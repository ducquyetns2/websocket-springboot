import './messenger.scss'
import ChatMenu from './chatMenu'
import ChatBox from './chatBox'
import ChatOnline from './chatOnline'
import { useEffect, useRef, useState, useCallback } from 'react'
import { socketUrl } from '../../store/constant'
import { useSelector } from 'react-redux'
import MessengerAPI from '../../services/MessengerAPI'
import UserAPI from '../../services/UserAPI'
import { Client } from '@stomp/stompjs'
import { useDispatch } from 'react-redux'
import { actions } from '../../store/reducer'

function Messenger() {
    const dispatch=useDispatch()
    var [stompClient,setStompClient] = useState(null)
    const [onlineUsers, setOnlineUsers] = useState([])
    const [users, setUsers] = useState([])
    const [conversations, setConversations] = useState([])
    const currentUser = useSelector(state => state.user)
    const [currentChat, setCurrentChat] = useState('')
    // Fetch all OnlineUsers
    async function getOnlineUsers() {
        let response=await UserAPI.getOnlineUsers()
        let onlineUsers=response.filter(user => user.id != currentUser.id)
        setOnlineUsers(onlineUsers)
    }
    // catch event after connected to web socket
    const onConnect=() => {
        console.log("connection success")
        stompClient?.publish({
            destination:"/app/chat.online",
            body: currentUser.id.toString()
        })
        stompClient?.subscribe("/topic/chat.online",payload => {
            console.log("doing online",JSON.parse(payload.body))
            getOnlineUsers()
        })
        stompClient?.subscribe("/topic/chat.offline",payload => {
            console.log("doing offline",JSON.parse(payload.body))
            getOnlineUsers()
        })
    }
     // Catch event when disconnect
     const onDisconnect=() => {
        
    }
     // Catch event when connection is fail
     const onError=() => {
        console.log("Connect to web socket error")
        stompClient?.forceDisconnect()
        stompClient?.deactivate()
    }
    // Create connection to web socket
    function connectWebsocket() {
        let client=new Client({
            brokerURL: socketUrl,
            connectHeaders: {
                Authorization: `Bearer ${currentUser.accessToken}`},
            })
        client.activate();
        // const disconnect=client.deactivate.bind(this)
        // dispatch(actions.setStomp(disconnect))
        return client;
    }
    useEffect(() => {
        setStompClient(connectWebsocket)
    }, [])
    // Catch event web socket
    useEffect(() => {
        if(stompClient && stompClient.active) {
            stompClient.onConnect=onConnect
            stompClient.onDisconnect=onDisconnect
            stompClient.onWebSocketError=onError
        }
        return () => {
                stompClient?.deactivate()
        }
    },[stompClient])
    // Fetch information from server
    useEffect(() => {
        (async () => {
            try {
                // Get all users
                const usersResponse = await UserAPI.getUsers()
                setUsers(usersResponse.filter(user => user._id !== currentUser.id))
                // Get all onlineUsers
                const onlineUsersResponse = await UserAPI.getOnlineUsers()
                setOnlineUsers(onlineUsersResponse.filter(user => user.id !== currentUser.id))
                // Get all conversations
                const conversationsResponse = await MessengerAPI.getConversations()
                setConversations(conversationsResponse)
            } catch(error) {

            }
        }
        )()
    }, [currentUser.id])
    return (
        <div className='container-fluid'>
            <div className='row messenger'>
                <div className='container_chatMenu col-lg-3'>
                    <ChatMenu conversations={conversations} currentUser={currentUser} onlineUsers={onlineUsers} setCurrentChat={setCurrentChat} stomp={stompClient}/>
                </div>
                <div className='container_chatBox col-lg-6'>
                    <ChatBox currentChat={currentChat} currentUser={currentUser} stomp={stompClient}/>
                </div>
                <div className='container_chatOnline col-lg-3'>
                    <ChatOnline onlineUsers={onlineUsers}
                        currentUser={currentUser} setCurrentChat={setCurrentChat} />
                </div>
            </div>
        </div>
    )
}
export default Messenger