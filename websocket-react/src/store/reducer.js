import { createSlice } from '@reduxjs/toolkit'

const initUser = {
    id: null,
    fullName:'',
    username: '',
    role: [],
    avatar:'',
    avatarPath: '',
    accessToken: '',
    stomp:null
}
const userReducer = createSlice({
    name: 'user',
    initialState: initUser,
    reducers: {
        login: (state, action) => {
            return action.payload 
        },
        logout: (state, action) => {
            return initUser
        },
        setStomp: (state,action) => {
            return {
                ...state,
                stomp: action.payload
            }
        }
    }
})
const { actions, reducer } = userReducer
export { actions }
export default reducer