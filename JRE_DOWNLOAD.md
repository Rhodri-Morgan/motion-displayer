You may be confronted with these error messages when trying to run the executable:


Note for `jre` this repository uses git lfs. In order to run the executable please download the lfs objects and not just the pointers.

To clone the repository to run properly use:

```shell
git lfs install --skip-repo
git lfs clone https://github.com/Rhodri-Morgan/Motion-Displayer
```

**Do not clone through GitHub UI 'Download Zip' as this does not respect lfs objects.**