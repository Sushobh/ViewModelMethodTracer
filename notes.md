## Publishing to Maven central using gpg.

1. Generate a GPG key,export it to secring file and publish to a key server.
   Noting down the the secring path is important because. I am not sure if the keys
   are automatically exported to secring file or not. So I export the keys manually just to be sure.
   The secring file path is important so take note of it by listing the keys.
```
gpg --gen-key
gpg --list-keys
gpg --keyserver keyserver.ubuntu.com --send-keys CA925CD6C9E8D064FF05B4728190C4130ABA0F98
gpg --export-secret-keys > gpg --export-secret-keys > C:/Users/Sushobh/AppData/Roaming/gnupg/secring.gpg
```
2. Then use it in global gradle.properties file
The properties file should look this
```
mavenCentralUsername=//The username obtained via generating the token
mavenCentralPassword=//The username obtained via generating the token
signing.keyId=//The last 8 digits of the key id.
signing.password=//The passpharase used while generating the gpg key.
signing.secretKeyRingFile=//The location of the secring file

```