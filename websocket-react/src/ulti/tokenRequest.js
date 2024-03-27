import axios from 'axios'
import { store } from '../store/store'
import { baseUrl,secretKey } from '../store/constant'
import jwtDecode from "jwt-decode";
import { actions} from "../store/reducer"
const tokenRequest = axios.create({
    baseURL: baseUrl
});
tokenRequest.interceptors.response.use((response) => {
    if(response.request.status==403) {
        window.location.href="/login";
    }
    return response.data;
})

tokenRequest.interceptors.request.use(config => {
    const accessToken = store.getState().user.accessToken;
        if( !accessToken ) window.location.href="/login";
        const decoded = jwtDecode(accessToken)
        const currentTime = new Date().getTime() / 1000;
        if (currentTime > decoded.exp) {
            console.log("token has been out of date")
            store.dispatch(actions.logout())
            window.location.href="/login";
        } else {
            config.headers.Authorization=`Bearer ${accessToken}`;
        }
    return config;
})
export default tokenRequest