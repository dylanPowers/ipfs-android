Release Build Instructions
===========================

To do a release, some environment variables must be set to do the signing.
The simplest way to configure this is to create a script called
```release.sh``` with the following contents:
```
export KEY_STORE_FILE=<keystore>
export KEY_STORE_PASSWORD=<pass>
export KEY_ALIAS=<alias>
export KEY_PASSWORD=<pass>

./gradlew assembleRelease

## Unset the variables so they're not sitting around for other scripts to see
unset KEY_STORE_FILE KEY_STORE_PASSWORD KEY_ALIAS KEY_PASSWORD
```