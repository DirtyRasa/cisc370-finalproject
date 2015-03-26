# Download and Install #

  * [TortoiseHg with Mercurial](http://mercurial.selenic.com/downloads/)
  * [Kdiff3](http://sourceforge.net/projects/kdiff3/files/kdiff3/0.9.95/KDiff3Setup_0.9.95-2.exe/download)
  * [WinMerge](http://sourceforge.net/projects/winmerge/files/stable/2.12.4/WinMerge-2.12.4-Setup.exe/download)

# Setup Environmental Path #
  1. Start -> Right Click Computer -> Properties
  1. Advanced System Settings
  1. Environment Variables...
  1. Under System Variables
    * Edit Path
    * Add the location where KDiff3 and WinMerge were installed

# Grab the source #
  1. Create a directory named "cisc370-finalproject"  (Or anything I suppose...)
  1. Right click the newly created folder
  1. TortoiseHg -> Clone...
    * Source: https://cisc370-finalproject.googlecode.com/hg/
    * Destination: (The folder you created in step 1)
  1. Click the clone button

# Setup TortoiseHg Settings #
  1. Right click any folder
  1. TortoiseHg -> Global Settings
  1. TortoiseHg settings
    * Three-way Merge Tool: winmergeu
    * Visual Diff Tool: kdiff3
  1. Workbench settings
    * After Pull Operation: update
  1. Commit settings
    * Close After Commit: True
    * (Project settings not global) Push After Commit: https://cisc370-finalproject.googlecode.com/hg/

# Commit and Re-base #
  * Always re-base before committing & pushing.
    * To re-base:
      1. Right click working directory -> Hg Workbench
      1. Check for incoming changes from selected URL
      1. Pull incoming changes from selected URL
      1. Right click the new change and merge with local...
    * To commit:
      1. Right click working directory -> Hg Commit...
      1. Add comments
      1. Click commit
      1. Enter username/password to push changes