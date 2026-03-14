# Changelog

## 1.0.0 (2026-03-14)


### ⚠ BREAKING CHANGES

* Enhance Reliability and Performance: Auto-install, Parallelism, and Path Robustness ([#44](https://github.com/casuffitsharp/sonar-ps-plugin/issues/44))

### Features

* abstract PowerShell script execution and improve thread safety ([#29](https://github.com/casuffitsharp/sonar-ps-plugin/issues/29)) ([eda34fb](https://github.com/casuffitsharp/sonar-ps-plugin/commit/eda34fb03fb9a7a7b982121118201c09431bd0da))
* ci: implement PSScriptAnalyzer caching and use variable for NuGet ver… ([#30](https://github.com/casuffitsharp/sonar-ps-plugin/issues/30)) ([820615d](https://github.com/casuffitsharp/sonar-ps-plugin/commit/820615d447d909467ec4896ce4eb2da37dcdb0b6))
* Enhance Reliability and Performance: Auto-install, Parallelism, and Path Robustness ([#44](https://github.com/casuffitsharp/sonar-ps-plugin/issues/44)) ([2ee1f51](https://github.com/casuffitsharp/sonar-ps-plugin/commit/2ee1f51df8a42c1463b7181cc0005c015e8153b4))
* implement Docker Compose test environment and refine rule quality mapping ([#37](https://github.com/casuffitsharp/sonar-ps-plugin/issues/37)) ([d7d5a15](https://github.com/casuffitsharp/sonar-ps-plugin/commit/d7d5a15718ccd99d3aba241397db2d15a051ed5d))
* Refactor cognitive complexity and halstead metrics ([#35](https://github.com/casuffitsharp/sonar-ps-plugin/issues/35)) ([959b33f](https://github.com/casuffitsharp/sonar-ps-plugin/commit/959b33f89ba7e905bdc27e5d0550395ee1474d2f))
* upgrade to java 21 and SQ 26.1 ([#22](https://github.com/casuffitsharp/sonar-ps-plugin/issues/22)) ([75e4fc6](https://github.com/casuffitsharp/sonar-ps-plugin/commit/75e4fc636e65739d96d8d191dd9f17c464e26896))
* upgrade to PSScriptAnalyzer 1.24 and update rules ([#21](https://github.com/casuffitsharp/sonar-ps-plugin/issues/21)) ([c10a897](https://github.com/casuffitsharp/sonar-ps-plugin/commit/c10a89712533cc45e59491765dcc893edb6e3afd))


### Bug Fixes

* **ci:** Set up JDK 17 for build ([fe33a13](https://github.com/casuffitsharp/sonar-ps-plugin/commit/fe33a136b0f79fe389b11ecf6e1864139cc4d3b6))
* **ci:** update azure pipeline to use modern vm images ([5e24d8a](https://github.com/casuffitsharp/sonar-ps-plugin/commit/5e24d8af008f036fe923ede113675c4650298799))
* create ContextWriteGuard to address shared syncronization context issues ([#28](https://github.com/casuffitsharp/sonar-ps-plugin/issues/28)) ([f8ced38](https://github.com/casuffitsharp/sonar-ps-plugin/commit/f8ced3801f7f9a5279fedacbeae9989f69028048))
* fix(security): addressing sonar security issues ([#27](https://github.com/casuffitsharp/sonar-ps-plugin/issues/27)) ([3371e49](https://github.com/casuffitsharp/sonar-ps-plugin/commit/3371e4953764a16e73910c3d6cdaa5d2d9167fd9))
* Resolve suppressed test failures and empty rule names in PowerShell analysis ([#49](https://github.com/casuffitsharp/sonar-ps-plugin/issues/49)) ([6249028](https://github.com/casuffitsharp/sonar-ps-plugin/commit/624902802d8fb71071ed81aebf5c82ea6750af2e))


### Miscellaneous Chores

* Bump actions/cache from 4 to 5 ([#24](https://github.com/casuffitsharp/sonar-ps-plugin/issues/24)) ([551696f](https://github.com/casuffitsharp/sonar-ps-plugin/commit/551696f0890929d0926833fdd899c093049b0bfc))
* Bump actions/cache from 4 to 5 ([#32](https://github.com/casuffitsharp/sonar-ps-plugin/issues/32)) ([1a4be65](https://github.com/casuffitsharp/sonar-ps-plugin/commit/1a4be65c2e939e01bb724e80e293ec9687543e47))
* Bump actions/checkout from 4 to 6 ([#13](https://github.com/casuffitsharp/sonar-ps-plugin/issues/13)) ([ec2ecc9](https://github.com/casuffitsharp/sonar-ps-plugin/commit/ec2ecc969a52fa8dc9ce3113ea37be660ff3ccda))
* Bump actions/checkout from 4 to 6 ([#25](https://github.com/casuffitsharp/sonar-ps-plugin/issues/25)) ([bbdcd13](https://github.com/casuffitsharp/sonar-ps-plugin/commit/bbdcd13d1873aca2cb15a566e35f97c00962453b))
* Bump actions/checkout from 4 to 6 ([#42](https://github.com/casuffitsharp/sonar-ps-plugin/issues/42)) ([c587f3e](https://github.com/casuffitsharp/sonar-ps-plugin/commit/c587f3e7c3a061056e0e24d9b0b4046fa6c6f6fd))
* Bump actions/setup-java from 4 to 5 ([#11](https://github.com/casuffitsharp/sonar-ps-plugin/issues/11)) ([2f7845d](https://github.com/casuffitsharp/sonar-ps-plugin/commit/2f7845d56bc85a49aee3bcb7527739299dd99952))
* Bump actions/setup-java from 4 to 5 ([#26](https://github.com/casuffitsharp/sonar-ps-plugin/issues/26)) ([91caa81](https://github.com/casuffitsharp/sonar-ps-plugin/commit/91caa817636d9b5f94d048484e9687135bafbc85))
* Bump actions/setup-java from 4 to 5 ([#38](https://github.com/casuffitsharp/sonar-ps-plugin/issues/38)) ([3e3d024](https://github.com/casuffitsharp/sonar-ps-plugin/commit/3e3d024d2dbdb311b68393d3442ae9214f7ee75b))
* Bump com.diffplug.spotless:spotless-maven-plugin from 2.43.0 to 3.3.0 in /sonar-ps-plugin ([#41](https://github.com/casuffitsharp/sonar-ps-plugin/issues/41)) ([1dd5099](https://github.com/casuffitsharp/sonar-ps-plugin/commit/1dd5099e2252e4b2d35b0a44bda98ea1c489635f))
* Bump com.sun.xml.bind:jaxb-impl from 3.0.0 to 4.0.6 in /sonar-ps-plugin ([#8](https://github.com/casuffitsharp/sonar-ps-plugin/issues/8)) ([b140ceb](https://github.com/casuffitsharp/sonar-ps-plugin/commit/b140cebec7bc248b276866170736851c71dfcc6b))
* Bump commons-io:commons-io from 2.11.0 to 2.14.0 in /sonar-ps-plugin ([#4](https://github.com/casuffitsharp/sonar-ps-plugin/issues/4)) ([643db38](https://github.com/casuffitsharp/sonar-ps-plugin/commit/643db38c6f5b3a216e3bf6b5236d1b35b7e970d0))
* Bump commons-io:commons-io from 2.14.0 to 2.21.0 in /sonar-ps-plugin ([#20](https://github.com/casuffitsharp/sonar-ps-plugin/issues/20)) ([91804fc](https://github.com/casuffitsharp/sonar-ps-plugin/commit/91804fc589fe9cbc633c11bc85fee0fd8ed69025))
* Bump hoverkraft-tech/compose-action from 2.0.1 to 2.5.0 ([#40](https://github.com/casuffitsharp/sonar-ps-plugin/issues/40)) ([4523594](https://github.com/casuffitsharp/sonar-ps-plugin/commit/452359405231afbee75d843cc30d27f93b795383))
* Bump jakarta.xml.bind:jakarta.xml.bind-api from 3.0.0 to 4.0.5 in /sonar-ps-plugin ([#9](https://github.com/casuffitsharp/sonar-ps-plugin/issues/9)) ([a748cc6](https://github.com/casuffitsharp/sonar-ps-plugin/commit/a748cc6b663c514d7174be670661be5f7dbf893b))
* Bump junit:junit from 4.13.1 to 4.13.2 in /sonar-ps-plugin ([#14](https://github.com/casuffitsharp/sonar-ps-plugin/issues/14)) ([64bf91f](https://github.com/casuffitsharp/sonar-ps-plugin/commit/64bf91f0c61c9c1a3f3e841de9378101e6b56ded))
* Bump org.apache.commons:commons-lang3 from 3.12.0 to 3.18.0 in /sonar-ps-plugin ([#3](https://github.com/casuffitsharp/sonar-ps-plugin/issues/3)) ([9415b74](https://github.com/casuffitsharp/sonar-ps-plugin/commit/9415b74c1e71a5926a2ad69856b49394c7fe4be0))
* Bump org.apache.commons:commons-lang3 from 3.18.0 to 3.20.0 in /sonar-ps-plugin ([#12](https://github.com/casuffitsharp/sonar-ps-plugin/issues/12)) ([bf889b0](https://github.com/casuffitsharp/sonar-ps-plugin/commit/bf889b04244878a51963e61d2d611ae9c84356a3))
* Bump org.apache.maven.plugins:maven-antrun-plugin from 3.1.0 to 3.2.0 in /sonar-ps-plugin ([#39](https://github.com/casuffitsharp/sonar-ps-plugin/issues/39)) ([c0f0286](https://github.com/casuffitsharp/sonar-ps-plugin/commit/c0f028634b2610e32ccba63c3b3bd41dfb901e2e))
* Bump org.apache.maven.plugins:maven-compiler-plugin from 3.5.1 to 3.15.0 in /sonar-ps-plugin ([#18](https://github.com/casuffitsharp/sonar-ps-plugin/issues/18)) ([13324fc](https://github.com/casuffitsharp/sonar-ps-plugin/commit/13324fc62845844117d2f07597358d60bffa1995))
* Bump org.mockito:mockito-core from 5.16.0 to 5.22.0 in /sonar-ps-plugin ([#33](https://github.com/casuffitsharp/sonar-ps-plugin/issues/33)) ([111ed87](https://github.com/casuffitsharp/sonar-ps-plugin/commit/111ed8766a642860e4ddecbf1c2aa4d0c24338ea))
* Bump org.mockito:mockito-core from 5.22.0 to 5.23.0 in /sonar-ps-plugin ([#43](https://github.com/casuffitsharp/sonar-ps-plugin/issues/43)) ([f648212](https://github.com/casuffitsharp/sonar-ps-plugin/commit/f6482127e7a258b6f4c026204a9e3498ef1f844e))
* Bump org.sonarsource.sonar-packaging-maven-plugin:sonar-packaging-maven-plugin from 1.16 to 1.25.1.3002 in /sonar-ps-plugin ([#16](https://github.com/casuffitsharp/sonar-ps-plugin/issues/16)) ([8d27b76](https://github.com/casuffitsharp/sonar-ps-plugin/commit/8d27b76660073cfda0144ea46c5acf0b59eb9338))
* chore: Add GitHub release automation and link legacy versions to original repo ([#2](https://github.com/casuffitsharp/sonar-ps-plugin/issues/2)) ([e3355c4](https://github.com/casuffitsharp/sonar-ps-plugin/commit/e3355c420240d34e3aafca52e0a02566c9f1d2bb))
* Configure Dependabot for Maven and GitHub Actions with daily schedule ([#7](https://github.com/casuffitsharp/sonar-ps-plugin/issues/7)) ([a402087](https://github.com/casuffitsharp/sonar-ps-plugin/commit/a402087a3e542ce3555e85c978ab28d7d545f088))
* consolidate integrity checks and update documentation ([#52](https://github.com/casuffitsharp/sonar-ps-plugin/issues/52)) ([b78ea98](https://github.com/casuffitsharp/sonar-ps-plugin/commit/b78ea9802f0fa3c82ffbc7c39b7dafe123db6406))
* fix release-please config ([#62](https://github.com/casuffitsharp/sonar-ps-plugin/issues/62)) ([701a87c](https://github.com/casuffitsharp/sonar-ps-plugin/commit/701a87c84927a2bdbcd6a66345a1cbdc252af160))
* Fix unrecognized &[#39](https://github.com/casuffitsharp/sonar-ps-plugin/issues/39);secrets&[#39](https://github.com/casuffitsharp/sonar-ps-plugin/issues/39); in sonar workflow job condition ([#47](https://github.com/casuffitsharp/sonar-ps-plugin/issues/47)) ([497acd3](https://github.com/casuffitsharp/sonar-ps-plugin/commit/497acd3224ae7f13c9aab3a12791a7a43bfa8b1f))
* improve release-please and dependabot configuration ([#65](https://github.com/casuffitsharp/sonar-ps-plugin/issues/65)) ([931fefa](https://github.com/casuffitsharp/sonar-ps-plugin/commit/931fefa4198ee5ffcb722428980e032fd391a2d2))
* Improve scanner token generation error handling ([#48](https://github.com/casuffitsharp/sonar-ps-plugin/issues/48)) ([2b1f312](https://github.com/casuffitsharp/sonar-ps-plugin/commit/2b1f312572e89a0edf543b888850b29668c6ad20))
* reorganize release-please configuration into dedicated directory ([#50](https://github.com/casuffitsharp/sonar-ps-plugin/issues/50)) ([12fe956](https://github.com/casuffitsharp/sonar-ps-plugin/commit/12fe956e92b6456cbfa596c8bc67119a833bd9df))
* setup sonar test coverage ([#23](https://github.com/casuffitsharp/sonar-ps-plugin/issues/23)) ([47b56b7](https://github.com/casuffitsharp/sonar-ps-plugin/commit/47b56b7a8630fcb4ad21541824e696ed299fe015))
* setup sonarqube analysis ([#15](https://github.com/casuffitsharp/sonar-ps-plugin/issues/15)) ([4d05e86](https://github.com/casuffitsharp/sonar-ps-plugin/commit/4d05e8672ae7d98afa29701937d7395ca64dd90c))
* Skip azure pipeline on non-code changes ([#46](https://github.com/casuffitsharp/sonar-ps-plugin/issues/46)) ([2a5e655](https://github.com/casuffitsharp/sonar-ps-plugin/commit/2a5e655824f5252f9916db5610fbe68424ddb1eb))
* Skip sonar analysis if SONAR_TOKEN is missing (e.g. dependabot) ([#45](https://github.com/casuffitsharp/sonar-ps-plugin/issues/45)) ([f16d754](https://github.com/casuffitsharp/sonar-ps-plugin/commit/f16d754938192154e943739287a882f02dd884d3))


### Build System

* refine release workflow and isolate test artifacts ([#60](https://github.com/casuffitsharp/sonar-ps-plugin/issues/60)) ([9810e71](https://github.com/casuffitsharp/sonar-ps-plugin/commit/9810e71770eca69d621814f6ebd44fd3f93348d2))


### Documentation

* documentation: add badges to readme ([#5](https://github.com/casuffitsharp/sonar-ps-plugin/issues/5)) ([235f0aa](https://github.com/casuffitsharp/sonar-ps-plugin/commit/235f0aa5f46a39f6495d63172f630fce79478058))
