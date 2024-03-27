import './homePage.scss'
import { useEffect, useState } from 'react'
import { useSelector, useDispatch } from 'react-redux'
import { USER, ADMIN, USER_KEY } from '../../store/constant'
import UserAPI from '../../services/UserAPI'
import { ToastContainer, toast } from 'react-toastify'

function HomePage() {
    const [users, setUsers] = useState([])
    const currentUser = useSelector(state => state.user)
    useEffect(() => {
        (async () => {
            try {
                const response = await UserAPI.getUsers()
                setUsers(response)
            } catch(error) {
                
            }
        })()
    }, [])
    const handleDeleteUser = async (id) => {
        const response = await UserAPI.deleteUser(id)
        if (!response.error) {
            setUsers(pre => pre.filter(user => user.id !== id))
            toast.success('Xóa tài khoản thành công', {
                position: "bottom-right",
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            })
        } else {
            toast.error('Xóa tài khoản thất bại', {
                position: "bottom-right",
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
                progress: undefined,
                theme: "light",
            })
        }
    }
    return (
        <div className='homePage'>
            <div className='container-lg homePage-content pt-5'>
                <table className="table table-hover table-light table-bordered text-wrap">
                    <caption className='caption-top text-white bg-dark text-center fs-4'>Quản lý tài khoản</caption>
                    <thead className='table-dark'>
                        <tr>
                            <th scope="col" className='text-center'>ID</th>
                            <th scope="col" className='text-center'>Tên đầy đủ</th>
                            <th scope="col" className='text-center'>Tên đăng nhập</th>
                            <th scope="col" className='text-center'>Vai trò</th>
                            <th scope="col" className='text-center'>Hành động</th>
                        </tr>
                    </thead>
                    <tbody className='align-middle'>
                        {users && users.map(item =>
                        (
                            <tr key={item.id}>
                                <th className='text-center'>{item.id}</th>
                                <td>{item.fullName}</td>
                                <td>{item.username}</td>
                                <td>{item?.roles?.reduce( (current,role)=>{
                                        return current + role.role
                               }
                                ,"" )}</td>
                                <td className='text-center'>
                                    <button className='btn px-5 btn-outline-danger'
                                        onClick={() => handleDeleteUser(item._id)}>Xóa</button>
                                </td>
                            </tr>
                        )
                        )}
                    </tbody>
                </table>
            </div>
            <ToastContainer
                position="bottom-right"
                autoClose={5000}
                hideProgressBar={false}
                newestOnTop={false}
                closeOnClick
                rtl={false}
                pauseOnFocusLoss
                draggable
                pauseOnHover
                theme="light"
            />
        </div>
    )
}
export default HomePage
