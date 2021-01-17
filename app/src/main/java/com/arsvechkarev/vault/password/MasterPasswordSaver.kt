package com.arsvechkarev.vault.password

interface MasterPasswordSaver {
  
  /** Returns saved master password or null if no master password is saved */
  fun getSavedMasterPassword(): String?
  
  /** Saves [masterPassword] locally*/
  fun saveMasterPassword(masterPassword: String)
}