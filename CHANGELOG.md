# Changelog

## [1.0.1](https://github.com/casuffitsharp/sonar-ps-plugin/compare/v1.0.0...v1.0.1) (2026-03-14)


### Bug Fixes

* enable draft releases to allow asset upload ([42b3798](https://github.com/casuffitsharp/sonar-ps-plugin/commit/42b379860894cc00d435c7c12d882bce1ce4a3b5))

## [1.0.0](https://github.com/casuffitsharp/sonar-ps-plugin/compare/v0.5.3...v1.0.0) (2026-03-14)


### ⚠ BREAKING CHANGES

* Enhance Reliability and Performance: Auto-install, Parallelism, and Path Robustness ([#44](https://github.com/casuffitsharp/sonar-ps-plugin/issues/44))

### Features

* abstract PowerShell script execution and improve thread safety ([#29](https://github.com/casuffitsharp/sonar-ps-plugin/issues/29)) ([29b1dbf](https://github.com/casuffitsharp/sonar-ps-plugin/commit/29b1dbf1259f1baaeef96efe740ecfe4262e975f))
* Enhance Reliability and Performance: Auto-install, Parallelism, and Path Robustness ([#44](https://github.com/casuffitsharp/sonar-ps-plugin/issues/44)) ([bd0e3e8](https://github.com/casuffitsharp/sonar-ps-plugin/commit/bd0e3e838545620533713263739a6c8ad163ac2b))
* implement Docker Compose test environment and refine rule quality mapping ([#37](https://github.com/casuffitsharp/sonar-ps-plugin/issues/37)) ([d75d8bd](https://github.com/casuffitsharp/sonar-ps-plugin/commit/d75d8bdf0af260b30570885b0a1803e0b43724d1))
* Refactor cognitive complexity and halstead metrics ([#35](https://github.com/casuffitsharp/sonar-ps-plugin/issues/35)) ([c0c336e](https://github.com/casuffitsharp/sonar-ps-plugin/commit/c0c336ea94afa37e751a71cdf49dfb8bb4bebae6))
* upgrade to java 21 and SQ 26.1 ([#22](https://github.com/casuffitsharp/sonar-ps-plugin/issues/22)) ([c3ba164](https://github.com/casuffitsharp/sonar-ps-plugin/commit/c3ba164ec5997d68c75591c7b8333d594078c740))
* upgrade to PSScriptAnalyzer 1.24 and update rules ([#21](https://github.com/casuffitsharp/sonar-ps-plugin/issues/21)) ([196603d](https://github.com/casuffitsharp/sonar-ps-plugin/commit/196603db0d6d4baf836ed95e117e10d3fea95845))


### Bug Fixes

* **ci:** Set up JDK 17 for build ([239101a](https://github.com/casuffitsharp/sonar-ps-plugin/commit/239101aae1208df6384520b9f282ad480adbe1ab))
* **ci:** update azure pipeline to use modern vm images ([441c8ab](https://github.com/casuffitsharp/sonar-ps-plugin/commit/441c8ab2a4b9d8aaedce0f40e71ad7f99ac6e949))
* compatibility with SonarQube 2025.3 ([7fcafd4](https://github.com/casuffitsharp/sonar-ps-plugin/commit/7fcafd4caec6a80c54000b2cac77469d883bdf68))
* create ContextWriteGuard to address shared syncronization context issues ([#28](https://github.com/casuffitsharp/sonar-ps-plugin/issues/28)) ([aeea963](https://github.com/casuffitsharp/sonar-ps-plugin/commit/aeea9639004566398a47f91a8535ee0b58ee695c))
* Resolve suppressed test failures and empty rule names in PowerShell analysis ([#49](https://github.com/casuffitsharp/sonar-ps-plugin/issues/49)) ([9e761a6](https://github.com/casuffitsharp/sonar-ps-plugin/commit/9e761a6c525c6c328a98354fdcb96546cba0a718))
* **security:** addressing sonar security issues ([#27](https://github.com/casuffitsharp/sonar-ps-plugin/issues/27)) ([3ba400e](https://github.com/casuffitsharp/sonar-ps-plugin/commit/3ba400ecb9f7ed623448f2ad7a8e7f5d7e545ad7))


### Build System

* consolidate integrity checks and documentation ([#52](https://github.com/casuffitsharp/sonar-ps-plugin/issues/52)) ([a0ebe0a](https://github.com/casuffitsharp/sonar-ps-plugin/commit/a0ebe0adb07c364a274691d2899d15ad939c964e))
* **deps:** Bump com.diffplug.spotless:spotless-maven-plugin from 2.43.0 to 3.3.0 in /sonar-ps-plugin ([#41](https://github.com/casuffitsharp/sonar-ps-plugin/issues/41)) ([a088ee7](https://github.com/casuffitsharp/sonar-ps-plugin/commit/a088ee71b0a0581a28e5aa1ad83408126cd52415))
* **deps:** Bump com.sun.xml.bind:jaxb-impl from 3.0.0 to 4.0.6 in /sonar-ps-plugin ([#8](https://github.com/casuffitsharp/sonar-ps-plugin/issues/8)) ([bf78bc0](https://github.com/casuffitsharp/sonar-ps-plugin/commit/bf78bc0b978f585dee6b53cb63521ad969afc5fb))
* **deps:** Bump commons-io:commons-io from 2.11.0 to 2.14.0 in /sonar-ps-plugin ([#4](https://github.com/casuffitsharp/sonar-ps-plugin/issues/4)) ([70e699d](https://github.com/casuffitsharp/sonar-ps-plugin/commit/70e699de80719cd7c743b7fedba4e995fbb7a45b))
* **deps:** Bump commons-io:commons-io from 2.14.0 to 2.21.0 in /sonar-ps-plugin ([#20](https://github.com/casuffitsharp/sonar-ps-plugin/issues/20)) ([f02be73](https://github.com/casuffitsharp/sonar-ps-plugin/commit/f02be73883ae23cb4c0e2f77ca3e1b868e5baa67))
* **deps:** Bump jakarta.xml.bind:jakarta.xml.bind-api from 3.0.0 to 4.0.5 in /sonar-ps-plugin ([#9](https://github.com/casuffitsharp/sonar-ps-plugin/issues/9)) ([71ba69d](https://github.com/casuffitsharp/sonar-ps-plugin/commit/71ba69dc165869640a87f0370ce9c05b7d3193b6))
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


### Documentation

* documentation: add badges to readme ([#5](https://github.com/casuffitsharp/sonar-ps-plugin/issues/5)) ([668522b](https://github.com/casuffitsharp/sonar-ps-plugin/commit/668522b77137e99e36ccbb5127bb34f772edc961))
* update documentation for fork continuation ([ae95a96](https://github.com/casuffitsharp/sonar-ps-plugin/commit/ae95a9609c4f4c318cc39adf8a20128ad0caeef0))
* update README.md ([041479c](https://github.com/casuffitsharp/sonar-ps-plugin/commit/041479c9460ce56a79a7298bf80a79741241b114))
* update release tag link in README to include v prefix ([#71](https://github.com/casuffitsharp/sonar-ps-plugin/issues/71)) ([0b33bee](https://github.com/casuffitsharp/sonar-ps-plugin/commit/0b33bee5d4fddc6968bc4f5a4133dd4527a633e6))
