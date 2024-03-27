import './user.scss'
import avatarDefault from '../../images/avatarDefault.png'
import { useState } from 'react'
import {useSelector} from 'react-redux'

function User({ user, addGroup, onClick, conversationId }) {
    const currentUser = useSelector(state => state.user)
    const [checked, setChecked] = useState(false)
    const handleClick = () => {
        if (addGroup) {
            setChecked(!checked)
        }
        // Handle transfer conversation
        if (onClick) {
            conversationId ? onClick(conversationId) : onClick(currentUser.username,user.username)
        }
    }
    return (
        <div className='user dropdown-item rounded' onClick={handleClick}>
            <div className='container_avatar'>
                <img className='user_avatar' src={user.avatarUrl || avatarDefault} />
                {user?.online && <span className='user-online'></span>}
            </div>
            <span className='user_name'>{user?.fullName || 'anonymous user'}</span>
            {addGroup && checked && <i className="fa-solid fa-check mt-1 ms-auto text-success"></i>}
        </div>
    )
}
export default User