# Changelog

## 1.0.0 (2026-03-14)


### ⚠ BREAKING CHANGES

* Enhance Reliability and Performance: Auto-install, Parallelism, and Path Robustness ([#44](https://github.com/casuffitsharp/sonar-ps-plugin/issues/44))

### Features

* abstract PowerShell script execution and improve thread safety ([#29](https://github.com/casuffitsharp/sonar-ps-plugin/issues/29)) ([29b1dbf](https://github.com/casuffitsharp/sonar-ps-plugin/commit/29b1dbf1259f1baaeef96efe740ecfe4262e975f))
* add cognitive complexity metric ([ccc9b05](https://github.com/casuffitsharp/sonar-ps-plugin/commit/ccc9b055233fd3720ea8840b6dd27b4a9f66831e))
* add cyclomatic complexity metric ([614db1c](https://github.com/casuffitsharp/sonar-ps-plugin/commit/614db1c35b444f039350dd7709c01ffa56210ef1))
* add default quality profile XML ([809a909](https://github.com/casuffitsharp/sonar-ps-plugin/commit/809a909e8a1bdc87e40497bbc6398c4134777838))
* add new constant ([779c1ed](https://github.com/casuffitsharp/sonar-ps-plugin/commit/779c1ede129d6dedadbef1c8a851a4ab1f7c4e00))
* add nloc and comment_lines metrics ([af90f01](https://github.com/casuffitsharp/sonar-ps-plugin/commit/af90f0136967316ed9b19393d4fd74a2078297e8))
* add parser results reader ([22dc472](https://github.com/casuffitsharp/sonar-ps-plugin/commit/22dc472a45abfc91df8418c2ce658f5812fd151a))
* add parser schema definition ([3537185](https://github.com/casuffitsharp/sonar-ps-plugin/commit/3537185f4b060607b53e4129afa0f352b76acf4d))
* add parser script for powershell ([ec74123](https://github.com/casuffitsharp/sonar-ps-plugin/commit/ec7412325715616291855105f92475208a5f2376))
* add rule PSUseSingularNouns ([#12](https://github.com/casuffitsharp/sonar-ps-plugin/issues/12)) ([4911624](https://github.com/casuffitsharp/sonar-ps-plugin/commit/4911624790b650b70d53e3b40e6ca4b39e9a67e5))
* allow file overrides during analysis ([cbf9c71](https://github.com/casuffitsharp/sonar-ps-plugin/commit/cbf9c712fa67b6bf8a74d9b2f9eb414d9634ee19))
* automated quality profile activation ([c247afa](https://github.com/casuffitsharp/sonar-ps-plugin/commit/c247afa53c470da9ffd88f498362feb7b290997c))
* Enhance Reliability and Performance: Auto-install, Parallelism, and Path Robustness ([#44](https://github.com/casuffitsharp/sonar-ps-plugin/issues/44)) ([bd0e3e8](https://github.com/casuffitsharp/sonar-ps-plugin/commit/bd0e3e838545620533713263739a6c8ad163ac2b))
* implement Docker Compose test environment and refine rule quality mapping ([#37](https://github.com/casuffitsharp/sonar-ps-plugin/issues/37)) ([d75d8bd](https://github.com/casuffitsharp/sonar-ps-plugin/commit/d75d8bdf0af260b30570885b0a1803e0b43724d1))
* implement executor service for parallelism ([7052967](https://github.com/casuffitsharp/sonar-ps-plugin/commit/705296778409ff7b4912c4bd16d6b9199e0e4bcb))
* implement tokenizer sensor ([8b3ce9f](https://github.com/casuffitsharp/sonar-ps-plugin/commit/8b3ce9f0c8b1069b11c1fdbfa2fc5f5bf30de82e))
* improve process builder for execution ([32c7beb](https://github.com/casuffitsharp/sonar-ps-plugin/commit/32c7beb08727a511ddc14d4ebc99ffbc4ef70f7a))
* improve syntax highlighting ([df4f0e6](https://github.com/casuffitsharp/sonar-ps-plugin/commit/df4f0e646abe4cc5f5e248053bc8327efc5850aa))
* initial commit ([641f9c1](https://github.com/casuffitsharp/sonar-ps-plugin/commit/641f9c1a485313524b0e86bacff33fcda15974ab))
* initial implementation ([fb11adb](https://github.com/casuffitsharp/sonar-ps-plugin/commit/fb11adb3d14ba431ef147484b4bd71369e538855))
* Refactor cognitive complexity and halstead metrics ([#35](https://github.com/casuffitsharp/sonar-ps-plugin/issues/35)) ([c0c336e](https://github.com/casuffitsharp/sonar-ps-plugin/commit/c0c336ea94afa37e751a71cdf49dfb8bb4bebae6))
* refactor issue decoration into separate class ([42bfd45](https://github.com/casuffitsharp/sonar-ps-plugin/commit/42bfd453ac42b8f9d832d22f4bd6dcfe33a2caed))
* refactor tokenization into separate classes ([55bec86](https://github.com/casuffitsharp/sonar-ps-plugin/commit/55bec868b427f1ba96b853d80c5663771567240b))
* refine powershell rules ([51157f1](https://github.com/casuffitsharp/sonar-ps-plugin/commit/51157f1138c187dd60f6030ac18bf131a21bf92e))
* update powershell rules and descriptions ([affcb46](https://github.com/casuffitsharp/sonar-ps-plugin/commit/affcb469aa637fc1bbe1a6afde682a5943a7dc66))
* update PowerShell tokenizer ([44a4a98](https://github.com/casuffitsharp/sonar-ps-plugin/commit/44a4a982c84eae81399436a84c19ff7c91b9b0ca))
* update powershell-rules.xml ([#30](https://github.com/casuffitsharp/sonar-ps-plugin/issues/30)) ([10163a5](https://github.com/casuffitsharp/sonar-ps-plugin/commit/10163a525b09de374ed096b10495a133d5f89381))
* update rule set ([44d0646](https://github.com/casuffitsharp/sonar-ps-plugin/commit/44d06461950f6dc68dc9a69276d62d930375e5e7))
* update rules definitions ([a528de4](https://github.com/casuffitsharp/sonar-ps-plugin/commit/a528de46d934843ca54ca630d6ab0a29f4561faf))
* update ruleset ([a199e3a](https://github.com/casuffitsharp/sonar-ps-plugin/commit/a199e3a3e25920e7bed04ccecd9f23841de0d09d))
* update sensor implementation ([24ed84c](https://github.com/casuffitsharp/sonar-ps-plugin/commit/24ed84c116d32580df27ef7e0e60cbe48b265d6f))
* upgrade to java 21 and SQ 26.1 ([#22](https://github.com/casuffitsharp/sonar-ps-plugin/issues/22)) ([c3ba164](https://github.com/casuffitsharp/sonar-ps-plugin/commit/c3ba164ec5997d68c75591c7b8333d594078c740))
* upgrade to PSScriptAnalyzer 1.24 and update rules ([#21](https://github.com/casuffitsharp/sonar-ps-plugin/issues/21)) ([196603d](https://github.com/casuffitsharp/sonar-ps-plugin/commit/196603db0d6d4baf836ed95e117e10d3fea95845))


### Bug Fixes

* **ci:** Set up JDK 17 for build ([239101a](https://github.com/casuffitsharp/sonar-ps-plugin/commit/239101aae1208df6384520b9f282ad480adbe1ab))
* **ci:** update azure pipeline to use modern vm images ([441c8ab](https://github.com/casuffitsharp/sonar-ps-plugin/commit/441c8ab2a4b9d8aaedce0f40e71ad7f99ac6e949))
* compatibility with SonarQube 2025.3 ([7fcafd4](https://github.com/casuffitsharp/sonar-ps-plugin/commit/7fcafd4caec6a80c54000b2cac77469d883bdf68))
* create ContextWriteGuard to address shared syncronization context issues ([#28](https://github.com/casuffitsharp/sonar-ps-plugin/issues/28)) ([aeea963](https://github.com/casuffitsharp/sonar-ps-plugin/commit/aeea9639004566398a47f91a8535ee0b58ee695c))
* fix typo in code ([#15](https://github.com/casuffitsharp/sonar-ps-plugin/issues/15)) ([7720a04](https://github.com/casuffitsharp/sonar-ps-plugin/commit/7720a0479887c79e7ba345ee81286507bb2d25a6))
* improve error handling logic ([7d98f99](https://github.com/casuffitsharp/sonar-ps-plugin/commit/7d98f9973a51d6eb5ee918402e20ba6b4139cc18))
* internal mistype ([3acca4f](https://github.com/casuffitsharp/sonar-ps-plugin/commit/3acca4f3d2c9818d97698c165256c9e3ea6f77ef))
* prevent process hanging on full output buffer ([#10](https://github.com/casuffitsharp/sonar-ps-plugin/issues/10)) ([b13c786](https://github.com/casuffitsharp/sonar-ps-plugin/commit/b13c78604fe8f62d79b42f49f4c670cf8b812ac9))
* resolve issue [#26](https://github.com/casuffitsharp/sonar-ps-plugin/issues/26) ([e5a58aa](https://github.com/casuffitsharp/sonar-ps-plugin/commit/e5a58aa789160a2832978ff402b8553343ab3b53))
* Resolve suppressed test failures and empty rule names in PowerShell analysis ([#49](https://github.com/casuffitsharp/sonar-ps-plugin/issues/49)) ([9e761a6](https://github.com/casuffitsharp/sonar-ps-plugin/commit/9e761a6c525c6c328a98354fdcb96546cba0a718))
* restore quotes in analysis file path ([#14](https://github.com/casuffitsharp/sonar-ps-plugin/issues/14)) ([78cecaa](https://github.com/casuffitsharp/sonar-ps-plugin/commit/78cecaae436ca39404eeb6f95ab24acc3d523f34))
* **security:** addressing sonar security issues ([#27](https://github.com/casuffitsharp/sonar-ps-plugin/issues/27)) ([3ba400e](https://github.com/casuffitsharp/sonar-ps-plugin/commit/3ba400ecb9f7ed623448f2ad7a8e7f5d7e545ad7))
* update error handling ([1b8c678](https://github.com/casuffitsharp/sonar-ps-plugin/commit/1b8c678e151ab43436b81e31a06c4c261854fb72))
* update token handling ([#5](https://github.com/casuffitsharp/sonar-ps-plugin/issues/5)) ([373e442](https://github.com/casuffitsharp/sonar-ps-plugin/commit/373e442de0c0cdcd7d4a0b262907320876aed8a2))
* various small code fixes ([160a2fc](https://github.com/casuffitsharp/sonar-ps-plugin/commit/160a2fcbbf58d49b52c601c627d7126e99d99659))
* various small code fixes ([61cbcdc](https://github.com/casuffitsharp/sonar-ps-plugin/commit/61cbcdc0a23ecab9c7326e7cadaf5de52a1b0bd9))


### Miscellaneous Chores

* general code cleanup ([3bed399](https://github.com/casuffitsharp/sonar-ps-plugin/commit/3bed39979581c5608d0b10e28d07b4638305f877))
* general code cleanup ([4de05c5](https://github.com/casuffitsharp/sonar-ps-plugin/commit/4de05c5b623d3a395a47980d3c35672b68846036))
* general code cleanup ([e27b8a2](https://github.com/casuffitsharp/sonar-ps-plugin/commit/e27b8a28177968f677bc30960e9a7048eb1f0fed))
* internal plugin updates ([9f08872](https://github.com/casuffitsharp/sonar-ps-plugin/commit/9f08872ba306dd604fd6fded68c40a40a88ff6ea))
* internal plugin updates ([9810f89](https://github.com/casuffitsharp/sonar-ps-plugin/commit/9810f891d7da0add46507425e028f6f04e2dc8b7))
* minor code updates ([eca8d1c](https://github.com/casuffitsharp/sonar-ps-plugin/commit/eca8d1ca3b8a3baeb482e206855457aa4aa5c178))
* miscellaneous changes before release ([c2705f3](https://github.com/casuffitsharp/sonar-ps-plugin/commit/c2705f35aee6d1107ee1bc6b23c3970106b198d4))
* prepare for new release ([5759dd3](https://github.com/casuffitsharp/sonar-ps-plugin/commit/5759dd36fb2f9d6e0036874e86d205d6abbb42ac))
* prepare for release ([718d3ab](https://github.com/casuffitsharp/sonar-ps-plugin/commit/718d3ab79ed35c01d144d0d32d6495e4c65baad7))
* rename internal variable ([cf7e810](https://github.com/casuffitsharp/sonar-ps-plugin/commit/cf7e810952340a334174184d6cca61dc85827856))
* simplify internal logic ([d8c661a](https://github.com/casuffitsharp/sonar-ps-plugin/commit/d8c661a4477e041cd8ff74a4c0360a131b8431c9))
* small miscellaneous changes ([6ece715](https://github.com/casuffitsharp/sonar-ps-plugin/commit/6ece715d20baa15fafedb0aaa8923786500a9218))
* small miscellaneous changes ([795a654](https://github.com/casuffitsharp/sonar-ps-plugin/commit/795a654fc7b3c7a02a5c2ff9688985475c43a180))
* update internal configuration ([eefe65d](https://github.com/casuffitsharp/sonar-ps-plugin/commit/eefe65db7d1af8dfdfccb33c2e9eca4a17f17421))


### Build System

* consolidate integrity checks and documentation ([#52](https://github.com/casuffitsharp/sonar-ps-plugin/issues/52)) ([a0ebe0a](https://github.com/casuffitsharp/sonar-ps-plugin/commit/a0ebe0adb07c364a274691d2899d15ad939c964e))
* **deps:** Bump com.diffplug.spotless:spotless-maven-plugin from 2.43.0 to 3.3.0 in /sonar-ps-plugin ([#41](https://github.com/casuffitsharp/sonar-ps-plugin/issues/41)) ([a088ee7](https://github.com/casuffitsharp/sonar-ps-plugin/commit/a088ee71b0a0581a28e5aa1ad83408126cd52415))
* **deps:** Bump com.sun.xml.bind:jaxb-impl from 3.0.0 to 4.0.6 in /sonar-ps-plugin ([#8](https://github.com/casuffitsharp/sonar-ps-plugin/issues/8)) ([bf78bc0](https://github.com/casuffitsharp/sonar-ps-plugin/commit/bf78bc0b978f585dee6b53cb63521ad969afc5fb))
* **deps:** Bump commons-io:commons-io from 2.11.0 to 2.14.0 in /sonar-ps-plugin ([#4](https://github.com/casuffitsharp/sonar-ps-plugin/issues/4)) ([70e699d](https://github.com/casuffitsharp/sonar-ps-plugin/commit/70e699de80719cd7c743b7fedba4e995fbb7a45b))
* **deps:** Bump commons-io:commons-io from 2.14.0 to 2.21.0 in /sonar-ps-plugin ([#20](https://github.com/casuffitsharp/sonar-ps-plugin/issues/20)) ([f02be73](https://github.com/casuffitsharp/sonar-ps-plugin/commit/f02be73883ae23cb4c0e2f77ca3e1b868e5baa67))
* **deps:** Bump jakarta.xml.bind:jakarta.xml.bind-api from 3.0.0 to 4.0.5 in /sonar-ps-plugin ([#9](https://github.com/casuffitsharp/sonar-ps-plugin/issues/9)) ([71ba69d](https://github.com/casuffitsharp/sonar-ps-plugin/commit/71ba69dc165869640a87f0370ce9c05b7d3193b6))
* **deps:** Bump junit from 4.12 to 4.13.1 in /sonar-ps-plugin ([#22](https://github.com/casuffitsharp/sonar-ps-plugin/issues/22)) ([0b99e56](https://github.com/casuffitsharp/sonar-ps-plugin/commit/0b99e565cb33d3121ab79fba7d0ccdbb88625eea))
* **deps:** Bump junit:junit from 4.13.1 to 4.13.2 in /sonar-ps-plugin ([#14](https://github.com/casuffitsharp/sonar-ps-plugin/issues/14)) ([eaa414b](https://github.com/casuffitsharp/sonar-ps-plugin/commit/eaa414ba9729d630eded3e14cee1f26fa1f50ac1))
* **deps:** Bump org.apache.commons:commons-lang3 from 3.12.0 to 3.18.0 in /sonar-ps-plugin ([#3](https://github.com/casuffitsharp/sonar-ps-plugin/issues/3)) ([901a922](https://github.com/casuffitsharp/sonar-ps-plugin/commit/901a922a102fa02638b9ad28323e52e9b045082c))
* **deps:** Bump org.apache.commons:commons-lang3 from 3.18.0 to 3.20.0 in /sonar-ps-plugin ([#12](https://github.com/casuffitsharp/sonar-ps-plugin/issues/12)) ([aa978c5](https://github.com/casuffitsharp/sonar-ps-plugin/commit/aa978c59afa3334d58db634d44d6eefac0f81094))
* **deps:** Bump org.apache.maven.plugins:maven-antrun-plugin from 3.1.0 to 3.2.0 in /sonar-ps-plugin ([#39](https://github.com/casuffitsharp/sonar-ps-plugin/issues/39)) ([51de116](https://github.com/casuffitsharp/sonar-ps-plugin/commit/51de116956cb4c99ff158a9ac053bed76e95c05e))
* **deps:** Bump org.apache.maven.plugins:maven-compiler-plugin from 3.5.1 to 3.15.0 in /sonar-ps-plugin ([#18](https://github.com/casuffitsharp/sonar-ps-plugin/issues/18)) ([f13a688](https://github.com/casuffitsharp/sonar-ps-plugin/commit/f13a688f84a6302ebb602704342302510c5d7197))
* **deps:** Bump org.mockito:mockito-core from 5.16.0 to 5.22.0 in /sonar-ps-plugin ([#33](https://github.com/casuffitsharp/sonar-ps-plugin/issues/33)) ([04d9609](https://github.com/casuffitsharp/sonar-ps-plugin/commit/04d960957d48fc482164304a1588d7b5871db69e))
* **deps:** Bump org.mockito:mockito-core from 5.22.0 to 5.23.0 in /sonar-ps-plugin ([#43](https://github.com/casuffitsharp/sonar-ps-plugin/issues/43)) ([b114f9b](https://github.com/casuffitsharp/sonar-ps-plugin/commit/b114f9b02c3217312f561704f238155a55158c73))
* **deps:** Bump org.sonarsource.sonar-packaging-maven-plugin:sonar-packaging-maven-plugin from 1.16 to 1.25.1.3002 in /sonar-ps-plugin ([#16](https://github.com/casuffitsharp/sonar-ps-plugin/issues/16)) ([7e064b3](https://github.com/casuffitsharp/sonar-ps-plugin/commit/7e064b3912f6e5d3e56581c1538bd8297b1a6a92))
* fix current version during release ([73a29f7](https://github.com/casuffitsharp/sonar-ps-plugin/commit/73a29f79a58a6752b8ed8daba58ad20b0dffc053))
* improve scanner token generation error handling ([#48](https://github.com/casuffitsharp/sonar-ps-plugin/issues/48)) ([e589a5e](https://github.com/casuffitsharp/sonar-ps-plugin/commit/e589a5e40b040ded29bbb572b7ddeab3088b8d52))
* refine release workflow and isolate test artifacts ([#60](https://github.com/casuffitsharp/sonar-ps-plugin/issues/60)) ([87d76b4](https://github.com/casuffitsharp/sonar-ps-plugin/commit/87d76b44143ce9d7c438b1aa9bc8fde8a75e89e5))
* update project version ([3027d93](https://github.com/casuffitsharp/sonar-ps-plugin/commit/3027d93b90d43c942ca0c1984957ae2abd770ea5))
* update project version ([0e2dc2d](https://github.com/casuffitsharp/sonar-ps-plugin/commit/0e2dc2d7fedb4777e6ea409100885d581876d943))
* update rule regeneration tooling ([40d8e23](https://github.com/casuffitsharp/sonar-ps-plugin/commit/40d8e232af765079aac280f2df41d6a7c2fd0948))
* update sonar-plugin-api version ([64fe0fb](https://github.com/casuffitsharp/sonar-ps-plugin/commit/64fe0fb8dbbf19c4c78c47ba47dee37a5d5c774d))
* update version ([a1bb042](https://github.com/casuffitsharp/sonar-ps-plugin/commit/a1bb04215932d49e45074afd0f979e3b92da375c))


### Documentation

* documentation: add badges to readme ([#5](https://github.com/casuffitsharp/sonar-ps-plugin/issues/5)) ([668522b](https://github.com/casuffitsharp/sonar-ps-plugin/commit/668522b77137e99e36ccbb5127bb34f772edc961))
* update documentation for fork continuation ([ae95a96](https://github.com/casuffitsharp/sonar-ps-plugin/commit/ae95a9609c4f4c318cc39adf8a20128ad0caeef0))
* update README ([20a1d52](https://github.com/casuffitsharp/sonar-ps-plugin/commit/20a1d52fbe87081f411ad6df2194dd50f4860a46))
* update README for release ([f6f5a1c](https://github.com/casuffitsharp/sonar-ps-plugin/commit/f6f5a1c436837975ea4b2dd6e4ac64be7ef9acbb))
* update README with supported properties ([d5759d7](https://github.com/casuffitsharp/sonar-ps-plugin/commit/d5759d7f1ccb3e3b300795a33a2674371231b6fb))
* update README.md ([041479c](https://github.com/casuffitsharp/sonar-ps-plugin/commit/041479c9460ce56a79a7298bf80a79741241b114))
* update README.md ([5b1abc6](https://github.com/casuffitsharp/sonar-ps-plugin/commit/5b1abc604bb1a2264e676ff64d901853e4f47b7f))
* update README.md ([8b84cdc](https://github.com/casuffitsharp/sonar-ps-plugin/commit/8b84cdcf35dc062965e93f7e9600728fbe359a24))
* update README.md ([4b14159](https://github.com/casuffitsharp/sonar-ps-plugin/commit/4b14159b2e4bdb6814ee42a8884d7e4f36c91d71))
* update README.md ([922cc78](https://github.com/casuffitsharp/sonar-ps-plugin/commit/922cc7874ec30c1f57bd82f733316471d1bfc320))
* update README.md ([122a235](https://github.com/casuffitsharp/sonar-ps-plugin/commit/122a23559f2aa065053127fa3a230aa53790fe62))
* update README.md ([a7e0f3f](https://github.com/casuffitsharp/sonar-ps-plugin/commit/a7e0f3f804f398b21c4f0a0e0d4441c963bfc8b2))
* update README.md with new options ([37ddd71](https://github.com/casuffitsharp/sonar-ps-plugin/commit/37ddd7126f8209df26a4496e1237385787f0fc61))
* update release tag link in README to include v prefix ([#71](https://github.com/casuffitsharp/sonar-ps-plugin/issues/71)) ([0b33bee](https://github.com/casuffitsharp/sonar-ps-plugin/commit/0b33bee5d4fddc6968bc4f5a4133dd4527a633e6))
