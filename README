#summary Guide to set-up source control.

= Download and Install =

 * [http://mercurial.selenic.com/downloads/ TortoiseHg with Mercurial]
 * [http://sourceforge.net/projects/kdiff3/files/kdiff3/0.9.95/KDiff3Setup_0.9.95-2.exe/download Kdiff3]
 * [http://sourceforge.net/projects/winmerge/files/stable/2.12.4/WinMerge-2.12.4-Setup.exe/download WinMerge]

= Setup Environmental Path =
 # Start -> Right Click Computer -> Properties
 # Advanced System Settings
 # Environment Variables...
 # Under System Variables
  * Edit Path
  * Add the location where KDiff3 and WinMerge were installed

= Grab the source =
  # Create a directory named "cisc370-finalproject"  (Or anything I suppose...)
  # Right click the newly created folder
  # TortoiseHg -> Commit...
   * Source: https://cisc370-finalproject.googlecode.com/hg/
   * Destination: (The folder you created in step 1)
  # Click the clone button

= Setup TortoiseHg Settings =
 # Right click any folder
 # TortoiseHg -> Global Settings
 # TortoiseHg settings
  * Three-way Merge Tool: winmergeu
  * Visual Diff Tool: kdiff3
 # Workbench settings
  * After Pull Operation: update
 # Commit settings
  * Close After Commit: True
  * (Project settings not global) Push After Commit: https://cisc370-finalproject.googlecode.com/hg/

= Commit and Re-base =
 * Always re-base before committing & pushing.
  * To re-base:
   # Right click working directory -> Hg Workbench
   # Check for incoming changes from selected URL
   # Pull incoming changes from selected URL
  * To commit:
   # Right click working directory -> Hg Commit...
   # Add comments
   # Click commit
   # Enter username/password to push changes