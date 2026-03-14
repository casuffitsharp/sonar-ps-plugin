# Changelog

## 1.0.0 (2026-03-14)


### ⚠ BREAKING CHANGES

* Enhance Reliability and Performance: Auto-install, Parallelism, and Path Robustness ([#44](https://github.com/casuffitsharp/sonar-ps-plugin/issues/44))

### Features

* abstract PowerShell script execution and improve thread safety ([#29](https://github.com/casuffitsharp/sonar-ps-plugin/issues/29)) ([bb66b57](https://github.com/casuffitsharp/sonar-ps-plugin/commit/bb66b57d5525ed6ffbde827f232ffe40d172e7f7))
* ci: implement PSScriptAnalyzer caching and use variable for NuGet ver… ([#30](https://github.com/casuffitsharp/sonar-ps-plugin/issues/30)) ([259327f](https://github.com/casuffitsharp/sonar-ps-plugin/commit/259327f180062888a7f263df2202fc55a06ecb7f))
* Enhance Reliability and Performance: Auto-install, Parallelism, and Path Robustness ([#44](https://github.com/casuffitsharp/sonar-ps-plugin/issues/44)) ([cb914d7](https://github.com/casuffitsharp/sonar-ps-plugin/commit/cb914d732d7a6038a55b518f88640ba49f44324e))
* implement Docker Compose test environment and refine rule quality mapping ([#37](https://github.com/casuffitsharp/sonar-ps-plugin/issues/37)) ([400b9f7](https://github.com/casuffitsharp/sonar-ps-plugin/commit/400b9f7a8579c46a3ec86178eb152ebfa0821ec5))
* Refactor cognitive complexity and halstead metrics ([#35](https://github.com/casuffitsharp/sonar-ps-plugin/issues/35)) ([5482f67](https://github.com/casuffitsharp/sonar-ps-plugin/commit/5482f67e4cebf9e1ce593b62571425542c247c9b))
* upgrade to java 21 and SQ 26.1 ([#22](https://github.com/casuffitsharp/sonar-ps-plugin/issues/22)) ([2b4b388](https://github.com/casuffitsharp/sonar-ps-plugin/commit/2b4b38852904f283323eef18e3f6dd7a6e007123))
* upgrade to PSScriptAnalyzer 1.24 and update rules ([#21](https://github.com/casuffitsharp/sonar-ps-plugin/issues/21)) ([629c6b1](https://github.com/casuffitsharp/sonar-ps-plugin/commit/629c6b1064e7ef3edac4366aca748db0578f8306))


### Bug Fixes

* **ci:** Set up JDK 17 for build ([a48d874](https://github.com/casuffitsharp/sonar-ps-plugin/commit/a48d8742bcaf551421e54416b455611a0673a5ec))
* **ci:** update azure pipeline to use modern vm images ([527a073](https://github.com/casuffitsharp/sonar-ps-plugin/commit/527a07302039686dbf90138021d144c3a8e7d29d))
* create ContextWriteGuard to address shared syncronization context issues ([#28](https://github.com/casuffitsharp/sonar-ps-plugin/issues/28)) ([b9ed7c2](https://github.com/casuffitsharp/sonar-ps-plugin/commit/b9ed7c2d6350aa91324781f4760bd39eaa42ef6c))
* fix(security): addressing sonar security issues ([#27](https://github.com/casuffitsharp/sonar-ps-plugin/issues/27)) ([a1ea397](https://github.com/casuffitsharp/sonar-ps-plugin/commit/a1ea39760e34de2b5333a6e1037fcb8bdb9482af))
* Resolve suppressed test failures and empty rule names in PowerShell analysis ([#49](https://github.com/casuffitsharp/sonar-ps-plugin/issues/49)) ([0a46471](https://github.com/casuffitsharp/sonar-ps-plugin/commit/0a46471ae2a3901423c2e456b7dd35605a126f38))


### Miscellaneous Chores

* chore: Add GitHub release automation and link legacy versions to original repo ([#2](https://github.com/casuffitsharp/sonar-ps-plugin/issues/2)) ([ff8e6cf](https://github.com/casuffitsharp/sonar-ps-plugin/commit/ff8e6cfdbdc962eb3fbd2778f3c4b5c4bba369ec))
* Configure Dependabot for Maven and GitHub Actions with daily schedule ([#7](https://github.com/casuffitsharp/sonar-ps-plugin/issues/7)) ([99aea09](https://github.com/casuffitsharp/sonar-ps-plugin/commit/99aea09d76ea9f163ced1fce8f64e4f805b6075c))
* consolidate integrity checks and update documentation ([#52](https://github.com/casuffitsharp/sonar-ps-plugin/issues/52)) ([23756dc](https://github.com/casuffitsharp/sonar-ps-plugin/commit/23756dc1431f7b4526f2144315d6ecca3a440ffe))
* **deps:** Bump hoverkraft-tech/compose-action from 2.0.1 to 2.5.0 ([#40](https://github.com/casuffitsharp/sonar-ps-plugin/issues/40)) ([833ec12](https://github.com/casuffitsharp/sonar-ps-plugin/commit/833ec12febc9a8f3b78857b98f2aa8d8fb9d2f04))
* fix release-please config ([#62](https://github.com/casuffitsharp/sonar-ps-plugin/issues/62)) ([3bfc24a](https://github.com/casuffitsharp/sonar-ps-plugin/commit/3bfc24a19678ceea5ca3aaa37da30334217e20de))
* Fix unrecognized &[#39](https://github.com/casuffitsharp/sonar-ps-plugin/issues/39);secrets&[#39](https://github.com/casuffitsharp/sonar-ps-plugin/issues/39); in sonar workflow job condition ([#47](https://github.com/casuffitsharp/sonar-ps-plugin/issues/47)) ([6e582d6](https://github.com/casuffitsharp/sonar-ps-plugin/commit/6e582d6826a6a2caab90c10e4abf380c431264cf))
* improve release-please and dependabot configuration ([#65](https://github.com/casuffitsharp/sonar-ps-plugin/issues/65)) ([f15c5f7](https://github.com/casuffitsharp/sonar-ps-plugin/commit/f15c5f745b64a1503dda99cdac98dbb0d719cf97))
* Improve scanner token generation error handling ([#48](https://github.com/casuffitsharp/sonar-ps-plugin/issues/48)) ([ab7fc1c](https://github.com/casuffitsharp/sonar-ps-plugin/commit/ab7fc1c1d2224edfd22c254c26dac47dc26cf708))
* reorganize release-please configuration into dedicated directory ([#50](https://github.com/casuffitsharp/sonar-ps-plugin/issues/50)) ([3b6abcf](https://github.com/casuffitsharp/sonar-ps-plugin/commit/3b6abcf1af37d982df84691aaf15e42343b6b5f1))
* setup sonar test coverage ([#23](https://github.com/casuffitsharp/sonar-ps-plugin/issues/23)) ([23ca801](https://github.com/casuffitsharp/sonar-ps-plugin/commit/23ca801eba6caca6aed6107a64ee1fc7991cceb9))
* setup sonarqube analysis ([#15](https://github.com/casuffitsharp/sonar-ps-plugin/issues/15)) ([f6749d4](https://github.com/casuffitsharp/sonar-ps-plugin/commit/f6749d4b70e359957ec6b1d3de2895dabee83f2a))
* Skip azure pipeline on non-code changes ([#46](https://github.com/casuffitsharp/sonar-ps-plugin/issues/46)) ([7b3a382](https://github.com/casuffitsharp/sonar-ps-plugin/commit/7b3a3823633ad36a510304322b5b3199282eacb9))
* Skip sonar analysis if SONAR_TOKEN is missing (e.g. dependabot) ([#45](https://github.com/casuffitsharp/sonar-ps-plugin/issues/45)) ([2fb7ff3](https://github.com/casuffitsharp/sonar-ps-plugin/commit/2fb7ff34c46fd8025269f8acccd9f76b836128ce))


### Build System

* **deps:** Bump com.diffplug.spotless:spotless-maven-plugin from 2.43.0 to 3.3.0 in /sonar-ps-plugin ([#41](https://github.com/casuffitsharp/sonar-ps-plugin/issues/41)) ([5253d77](https://github.com/casuffitsharp/sonar-ps-plugin/commit/5253d777da7b35ec26741f5de254778aa47be0ea))
* **deps:** Bump com.sun.xml.bind:jaxb-impl from 3.0.0 to 4.0.6 in /sonar-ps-plugin ([#8](https://github.com/casuffitsharp/sonar-ps-plugin/issues/8)) ([767c651](https://github.com/casuffitsharp/sonar-ps-plugin/commit/767c651f20f4c17701c611e439b0ba062fc30cec))
* **deps:** Bump commons-io:commons-io from 2.11.0 to 2.14.0 in /sonar-ps-plugin ([#4](https://github.com/casuffitsharp/sonar-ps-plugin/issues/4)) ([3d77a3e](https://github.com/casuffitsharp/sonar-ps-plugin/commit/3d77a3e6667b4fd496d371001b8491c94fdf424c))
* **deps:** Bump commons-io:commons-io from 2.14.0 to 2.21.0 in /sonar-ps-plugin ([#20](https://github.com/casuffitsharp/sonar-ps-plugin/issues/20)) ([e213d2a](https://github.com/casuffitsharp/sonar-ps-plugin/commit/e213d2a1fcbb22607783d8aa3e4fa4bdc8130394))
* **deps:** Bump jakarta.xml.bind:jakarta.xml.bind-api from 3.0.0 to 4.0.5 in /sonar-ps-plugin ([#9](https://github.com/casuffitsharp/sonar-ps-plugin/issues/9)) ([e3fcb2f](https://github.com/casuffitsharp/sonar-ps-plugin/commit/e3fcb2f5f646f373181131522da896ca63bf49dc))
* **deps:** Bump junit from 4.12 to 4.13.1 in /sonar-ps-plugin ([#22](https://github.com/casuffitsharp/sonar-ps-plugin/issues/22)) ([17840a5](https://github.com/casuffitsharp/sonar-ps-plugin/commit/17840a5df0c52e330776cc8b7ba194b93d537ca0))
* **deps:** Bump junit:junit from 4.13.1 to 4.13.2 in /sonar-ps-plugin ([#14](https://github.com/casuffitsharp/sonar-ps-plugin/issues/14)) ([c578c55](https://github.com/casuffitsharp/sonar-ps-plugin/commit/c578c55c2086ebfd69ffb0e1351790e637e964c7))
* **deps:** Bump org.apache.commons:commons-lang3 from 3.12.0 to 3.18.0 in /sonar-ps-plugin ([#3](https://github.com/casuffitsharp/sonar-ps-plugin/issues/3)) ([137ba8f](https://github.com/casuffitsharp/sonar-ps-plugin/commit/137ba8f1059fb37fce606073889ffb4c4870b6bc))
* **deps:** Bump org.apache.commons:commons-lang3 from 3.18.0 to 3.20.0 in /sonar-ps-plugin ([#12](https://github.com/casuffitsharp/sonar-ps-plugin/issues/12)) ([81ff7ef](https://github.com/casuffitsharp/sonar-ps-plugin/commit/81ff7ef4ca9844cd927996dd969ff97347dce538))
* **deps:** Bump org.apache.maven.plugins:maven-antrun-plugin from 3.1.0 to 3.2.0 in /sonar-ps-plugin ([#39](https://github.com/casuffitsharp/sonar-ps-plugin/issues/39)) ([bfe28e5](https://github.com/casuffitsharp/sonar-ps-plugin/commit/bfe28e52aef7b8939d268f78a501a9a2194e41fa))
* **deps:** Bump org.apache.maven.plugins:maven-compiler-plugin from 3.5.1 to 3.15.0 in /sonar-ps-plugin ([#18](https://github.com/casuffitsharp/sonar-ps-plugin/issues/18)) ([d48bcc9](https://github.com/casuffitsharp/sonar-ps-plugin/commit/d48bcc9d81aa66fa29769ced4a784bd8c474af7e))
* **deps:** Bump org.mockito:mockito-core from 5.16.0 to 5.22.0 in /sonar-ps-plugin ([#33](https://github.com/casuffitsharp/sonar-ps-plugin/issues/33)) ([a292025](https://github.com/casuffitsharp/sonar-ps-plugin/commit/a292025ee16ea95c73c4166cfd611d01c9bf2a52))
* **deps:** Bump org.mockito:mockito-core from 5.22.0 to 5.23.0 in /sonar-ps-plugin ([#43](https://github.com/casuffitsharp/sonar-ps-plugin/issues/43)) ([6c0055a](https://github.com/casuffitsharp/sonar-ps-plugin/commit/6c0055a0e22cce769fe8da13da65c042cb7d0fb1))
* **deps:** Bump org.sonarsource.sonar-packaging-maven-plugin:sonar-packaging-maven-plugin from 1.16 to 1.25.1.3002 in /sonar-ps-plugin ([#16](https://github.com/casuffitsharp/sonar-ps-plugin/issues/16)) ([0382713](https://github.com/casuffitsharp/sonar-ps-plugin/commit/0382713e36fcebfd974cfb26f53a2366b6508201))
* refine release workflow and isolate test artifacts ([#60](https://github.com/casuffitsharp/sonar-ps-plugin/issues/60)) ([5151a3a](https://github.com/casuffitsharp/sonar-ps-plugin/commit/5151a3ac713fe096f02c429a8089767404cd7815))


### Documentation

* documentation: add badges to readme ([#5](https://github.com/casuffitsharp/sonar-ps-plugin/issues/5)) ([beb7e70](https://github.com/casuffitsharp/sonar-ps-plugin/commit/beb7e704637a81ef37556b591422550fb09ec3a5))
