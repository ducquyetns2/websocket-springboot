import './message.scss'
import avatarDefault from '../../images/avatarDefault.png'
import TimeAgo from 'javascript-time-ago'
import vi from 'javascript-time-ago/locale/vi'
import { useEffect, useState, useRef } from 'react'
import UserAPI from '../../services/UserAPI'
TimeAgo.addDefaultLocale(vi)

function Message({ message, own }) {
    const timeAgo = useRef(new TimeAgo('vi'))
    return (
        <div className={own ? 'message own' : 'message'} >
            <div className='messageTop'>
                <img src={message?.sender.avatarUrl || avatarDefault} className='messageImg' />
                <span className='messageText'>{message?.message}</span>
            </div>
            <div className='messageBottom '>
                {timeAgo.current.format(new Date(message?.createdDate))}
            </div>
        </div>
    )
}
export default Message