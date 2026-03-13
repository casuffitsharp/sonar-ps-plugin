import subprocess
import os

REPO_PATH = r'c:\Users\hfazevedo\repos\sonar-ps-plugin'
MAPPING_FILE = os.path.join(REPO_PATH, 'commit_mapping.md')
NEW_BRANCH = 'main-rewritten'

def run_git(args):
    return subprocess.check_output(['git'] + args, cwd=REPO_PATH, universal_newlines=True, stderr=subprocess.STDOUT).strip()

def get_mapping():
    mapping = {}
    if not os.path.exists(MAPPING_FILE): return {}
    with open(MAPPING_FILE, 'r', encoding='utf-8') as f:
        for line in f:
            if line.startswith('| ') and 'SHA' not in line and '---|---' not in line:
                parts = [p.strip() for p in line.split('|')]
                if len(parts) >= 5:
                    mapping[parts[1]] = parts[3]
    return mapping

def is_merge_commit(sha):
    return len(run_git(['log', '-1', '--format=%P', sha]).split()) > 1

def rewrite():
    msg_map = get_mapping()
    all_commits = run_git(['log', '--reverse', '--format=%H', 'main']).split('\n')
    print(f"Total commits in main: {len(all_commits)}")
    
    # 1. Start orphan
    run_git(['checkout', '--orphan', 'temp-orphan'])
    run_git(['rm', '-rf', '.'])
    
    # 2. Apply first
    first_sha = all_commits[0]
    print(f"Applying root {first_sha[:7]}...")
    try:
        run_git(['cherry-pick', first_sha])
    except subprocess.CalledProcessError as e:
        print(f"Root cherry-pick conflict: {e.output}")
        # Resolve root conflict by simply taking the commit state
        run_git(['reset', '--hard', first_sha])

    with open(os.path.join(REPO_PATH, '.gitignore'), 'a') as f:
        f.write('\n.settings/\n.vscode/\nbin/\ntarget/\n')
    run_git(['add', '.gitignore'])
    
    first_msg = msg_map.get(first_sha[:7], "Initial commit")
    run_git(['commit', '--amend', '--no-gpg-sign', '-m', first_msg])
    
    # 3. Rename immediately to avoid checking out orphaning issues
    try: run_git(['branch', '-D', NEW_BRANCH])
    except: pass
    run_git(['branch', '-m', NEW_BRANCH])
    
    counter = 1
    for i, sha in enumerate(all_commits[1:]):
        short_sha = sha[:7]
        args = ['cherry-pick', '--no-gpg-sign']
        if is_merge_commit(sha): args += ['-m', '1']
        args.append(sha)
        
        try:
            subprocess.check_output(['git'] + args, cwd=REPO_PATH, stderr=subprocess.STDOUT, universal_newlines=True)
            new_msg = msg_map.get(short_sha, run_git(['log', '-1', '--format=%s', sha]))
            run_git(['commit', '--amend', '--no-gpg-sign', '-m', new_msg])
            counter += 1
            if counter % 10 == 0: print(f"Progress: {counter}/{len(all_commits)}")
        except subprocess.CalledProcessError as e:
            if "nothing to commit" in e.output or "The previous cherry-pick is now empty" in e.output:
                try: run_git(['cherry-pick', '--skip'])
                except: pass
                continue
            
            # Resolve conflict with --theirs to maintain target state
            run_git(['checkout', '--theirs', '.'])
            run_git(['add', '.'])
            new_msg = msg_map.get(short_sha, run_git(['log', '-1', '--format=%s', sha]))
            
            # Check if we still have something to commit
            try:
                run_git(['commit', '--no-gpg-sign', '-m', new_msg])
                counter += 1
            except subprocess.CalledProcessError as e2:
                if "nothing to commit" in e2.output:
                    print(f"Skipping {short_sha} (redundant after resolution)")
                    try: run_git(['cherry-pick', '--skip'])
                    except: pass
                else:
                    print(f"Critical error at {short_sha}")
                    raise e2
            
    print(f"FINISHED. {counter} commits in total.")

if __name__ == "__main__":
    rewrite()
