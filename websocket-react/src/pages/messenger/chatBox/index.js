import './chatBox.scss'
import Message from '../../../components/message'
import { useState, useEffect, useRef } from 'react'
import MessengerAPI from '../../../services/MessengerAPI'

function ChatBox({currentChat, currentUser,stomp}) {
    const [chat, setChat] = useState('')
    const [messages, setMessages] = useState([])
    const sendError = useRef()
    const scrollRef = useRef()
    useEffect(() => {
        (async () => {
            if (currentChat) {
                let response = await MessengerAPI.getMessagesByConversationId(currentChat)
                try {
                    setMessages(response)
                }catch(e) {

                }
            }
        })()
    }, [currentChat])
    // Listen receive message
    useEffect(() => {
        let receiveSub=stomp?.connected && stomp?.subscribe(`/user/${currentUser.username}/queue/chat.receiveMessage`,payload => {
            setMessages(pre => {
                return [...pre,JSON.parse(payload.body)]
            })
        })
        return () => {
            stomp?.connected && stomp?.unsubscribe(receiveSub)
        }
    }, [stomp?.connected])
    useEffect(() => {
        scrollRef.current?.scrollIntoView({
            behavior: 'smooth'
        })
    }, [messages.length])
    const handleCreateMessage = (e) => {
        sendError.current.innerText = ''
        setChat(e.target.value)
    }
    const handleSendMessage = async (e) => {
        e.preventDefault()
        if (chat) {
            try {
                sendError.current.innerText = ''
                stomp?.connected && stomp?.publish({
                    destination: '/app/chat.sendMessage',
                    body: JSON.stringify({
                        conversationId: currentChat,
                        sender: currentUser.username,
                        message: chat
                    })
                })
                setChat('')
            } catch {

            }
        } else {
            sendError.current.innerText = 'Message is empty'
        }
    }
    return (
        <div className='chatBox border-top-0 border-bottom-0'>
            {currentChat ?
                <div className='container_chatBox'>
                    <div className='chatBoxTop'>
                        {messages?.map((message =>
                            <div key={message?.id} ref={scrollRef}>
                                <Message message={message} own={(message?.sender.username === currentUser.username)} />
                            </div>
                        ))}
                    </div>
                    <form className='chatBoxBottom d-flex mt-3 mx-1'>
                        <input className='form-control' onChange={handleCreateMessage} value={chat} />
                        <button className='btn btn-success ms-2' onClick={handleSendMessage}>Send</button>
                    </form>
                    <p ref={sendError} style={{ color: 'red' }}></p>
                </div> :
                <h5 className='text-center'>Open a coversation to chat</h5>}
        </div>
    )
}
export default ChatBox