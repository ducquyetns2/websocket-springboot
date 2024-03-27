import { configureStore } from '@reduxjs/toolkit'
import {
    persistReducer, persistStore,
    FLUSH,
    REHYDRATE,
    PAUSE,
    PERSIST,
    PURGE,
    REGISTER,
} from 'redux-persist'
import { combineReducers } from '@reduxjs/toolkit'
import storage from 'redux-persist/lib/storage'
import userReducer from './reducer'

const rootReducer = combineReducers({
    user: userReducer
})
const persistConfig = {
    key: 'root',
    storage
}
let persistedReducer = persistReducer(persistConfig, rootReducer)
let store = configureStore({
    reducer: persistedReducer,
    middleware: (getDefaultMiddleware) =>
        getDefaultMiddleware({
            serializableCheck: {
                ignoredActions: [FLUSH, REHYDRATE, PAUSE, PERSIST, PURGE, REGISTER],
            }
        })
})
let persistor = persistStore(store)
export { store, persistor }
