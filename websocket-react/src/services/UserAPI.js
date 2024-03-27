import request from '../ulti/request'
import tokenRequest from '../ulti/tokenRequest'

class UserAPI {
    register(data, options) {
        return request.post('/users', data, options)
    }
    login(data,options) {
        return request.post("/login",data,options)
    }
    logout() {
        return tokenRequest.get("/logout")
    }
    getUsers(options) {
        return tokenRequest.get("/users",options)
    }
    getOnlineUsers(options) {
        return tokenRequest.get("/users/online",options)
    }
    getUserById(userId,options) {
        return tokenRequest.get(`/users/${userId}`,options)
    }
}
export default new UserAPI()