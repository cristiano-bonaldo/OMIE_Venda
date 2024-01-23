package cvb.com.br.vendaomie.di

import cvb.com.br.vendaomie.data.data_source.local.LocalItemSaleDataSource
import cvb.com.br.vendaomie.data.data_source.local.LocalSaleDataSource
import cvb.com.br.vendaomie.data.repository.ItemSaleRepositoryImpl
import cvb.com.br.vendaomie.data.repository.SaleRepositoryImpl
import cvb.com.br.vendaomie.domain.data_source.ItemSaleDataSource
import cvb.com.br.vendaomie.domain.data_source.SaleDataSource
import cvb.com.br.vendaomie.domain.repository.ItemSaleRepository
import cvb.com.br.vendaomie.domain.repository.SaleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModuleBind {

    @Singleton
    @Binds
    abstract fun bindSaleDataSource(dataSource: LocalSaleDataSource): SaleDataSource

    @Binds
    @Singleton
    abstract fun bindSaleRepository(repository: SaleRepositoryImpl): SaleRepository

    @Singleton
    @Binds
    abstract fun bindItemSaleDataSource(dataSource: LocalItemSaleDataSource): ItemSaleDataSource

    @Binds
    @Singleton
    abstract fun bindItemSaleRepository(repository: ItemSaleRepositoryImpl): ItemSaleRepository
}

