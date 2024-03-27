import './chatOnline.scss'
import User from '../../../components/user'
import { useEffect, useState } from 'react'
import MessengerAPI from '../../../services/MessengerAPI'

function ChatOnline({ onlineUsers, setCurrentChat }) {
    // handle transfer conversationId
    const handleChat = async (currentUser, otherUser) => {
        let conversation = await MessengerAPI.getOrCreateConversationByMembers(currentUser,otherUser)
        try {
            setCurrentChat(conversation.id)
        } catch {
        }
    }
    return (
        <div className='chatOnline'>
            <h6 className='text-muted ms-2'>Online Users</h6>
            <div className='users-online'>
                {
                onlineUsers?.map(onlineUser => 
                    <User user={onlineUser} onClick={handleChat} key={onlineUser.id}/>)
                }
            </div >
        </div >
    )
}
export default ChatOnline