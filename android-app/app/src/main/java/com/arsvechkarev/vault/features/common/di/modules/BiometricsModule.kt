package com.arsvechkarev.vault.features.common.di.modules

import com.arsvechkarev.vault.features.common.biometrics.BiometricsAllowedManager
import com.arsvechkarev.vault.features.common.biometrics.BiometricsAllowedManagerImpl
import com.arsvechkarev.vault.features.common.biometrics.BiometricsAvailabilityProvider
import com.arsvechkarev.vault.features.common.biometrics.BiometricsAvailabilityProviderImpl
import com.arsvechkarev.vault.features.common.biometrics.BiometricsCipherProvider
import com.arsvechkarev.vault.features.common.biometrics.BiometricsCipherProviderImpl
import com.arsvechkarev.vault.features.common.biometrics.BiometricsEnabledProvider
import com.arsvechkarev.vault.features.common.biometrics.BiometricsStorage
import com.arsvechkarev.vault.features.common.biometrics.BiometricsStorageImpl

interface BiometricsModule {
  val biometricsAvailabilityProvider: BiometricsAvailabilityProvider
  val biometricsEnabledProvider: BiometricsEnabledProvider
  val biometricsStorage: BiometricsStorage
  val biometricsCipherProvider: BiometricsCipherProvider
  val biometricsAllowedManager: BiometricsAllowedManager
}

class BiometricsModuleImpl(
  coreModule: CoreModule,
  preferencesModule: PreferencesModule
) : BiometricsModule {
  
  override val biometricsAvailabilityProvider =
      BiometricsAvailabilityProviderImpl(coreModule.application)
  
  private val biometricsStorageImpl =
      BiometricsStorageImpl(preferencesModule.biometricsDataPreferences)
  
  override val biometricsEnabledProvider = biometricsStorageImpl
  
  override val biometricsStorage = biometricsStorageImpl
  
  override val biometricsAllowedManager = BiometricsAllowedManagerImpl(
    coreModule.timestampProvider,
    preferencesModule.biometricsMetadataPreferences
  )
  
  override val biometricsCipherProvider = BiometricsCipherProviderImpl()
}
