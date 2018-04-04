package test.nicolasjafelle.kithub.module

import test.nicolasjafelle.kithub.repository.Repository
import test.nicolasjafelle.kithub.repository.RepositoryImpl
import test.nicolasjafelle.kithub.ui.main.MainPresenter
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext


val module: Module = applicationContext {
    factory { MainPresenter(get()) }
    bean { RepositoryImpl() as Repository }

}