import axios from 'axios'
import { baseUrl } from '../store/constant'

const request = axios.create({
    baseURL: baseUrl
})
request.interceptors.response.use((response) => {
    return response.data
})
export default request