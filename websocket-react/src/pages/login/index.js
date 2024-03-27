import './login.scss'
import { Formik, Form, Field } from 'formik'
import { useRef, forwardRef } from 'react'
import { useNavigate } from 'react-router-dom'
import FormControl from '../../components/formControl'
import * as yup from 'yup'
import { ToastContainer, toast } from 'react-toastify'
import { useDispatch } from 'react-redux'
import { actions } from '../../store/reducer'
import UserAPI from '../../services/UserAPI'

function FormLog() {
    const dispatch = useDispatch()
    let schemaLogin = yup.object().shape({
        username: yup.string().required('Vui lòng nhập trường này'),
        password: yup.string().required('Vui lòng nhập trường này')
    })
    let schemaRegister = yup.object().shape({
        fullName: yup.string().required('Vui lòng nhập trường này'),
        username: yup.string().required('Vui lòng nhập trường này'),
        password: yup.string().required('Vui lòng nhập trường này').min(5, 'Vui lòng nhập tối thiểu 5 kí tự'),
        rePassword: yup.string().required('Vui lòng nhập trường này').oneOf([yup.ref('password'), null], 'Mật khẩu nhập lại không đúng'),
        avatar: yup.mixed().required('Vui lòng chọn avatar')
    })
    const errorLogin = useRef()
    const errorRegister = useRef()
    const registerAvatar = useRef()
    const initialLogin = {
        username: '',
        password: '',
    }
    const initialRegister = {
        fullName:'',
        username: '',
        password: '',
        rePassword: '',
        avatar: ''
    }
    const navigate = useNavigate()
    const handleLogin = async (values) => {
        try {
            // const response = await UserAPI.login(values)
            // errorLogin.current.textContent = ''
        fetch("http://localhost:8000/api/v1/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(values)
        }).then(resp => resp.json()).then(data => {
            dispatch(actions.login(data))
            navigate('/')
        }).catch(error => {
            errorLogin.current.textContent = 'Tên đăng nhập hoặc mật khẩu không chính xác'
        })
        } catch(error) {
        }
    }
    const handleRegister = async (values, { resetForm }) => {
        try {
            const response = await UserAPI.register(values, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            })
                errorRegister.current.innerText = ''
                resetForm()
                registerAvatar.current.value = ""
                toast.success('Tạo tài khoản thành công', {
                    position: "bottom-right",
                    autoClose: 5000,
                    hideProgressBar: false,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: true,
                    progress: undefined,
                    theme: "light",
                });  
        }
        catch(error) {
            errorRegister.current.innerText = error.message
        }
    }
    const resetMessage = () => {
        errorLogin.current.innerText = ''
        errorRegister.current.innerText = ''
    }
    const registerName = useRef()
    const resetForm = (props) => {
        props.resetForm()
        registerAvatar.current.value = null
    }
    return (
        <div className='form pt-5'>
            <div className='form_login container shadow-lg rounded'>
                <Formik
                    initialValues={initialLogin}
                    validationSchema={schemaLogin}
                    onSubmit={handleLogin}
                >
                    {(props) => {
                        return (
                            <Form className='row px-5' onChange={resetMessage}>
                                <h2 className='text-center mt-3'>Đăng nhập</h2>
                                <div className='col-12 d-flex justify-content-end'>
                                    <button type='button' className='btn btn-outline-danger' data-bs-toggle='modal' data-bs-target='#modal_register'>Đăng ký</button>
                                </div>
                                <Field type='text' name='username' id='username-login' placeholder='Enter this field' label='Tên đăng nhập'
                                    component={FormControl} />
                                <Field type='password' name='password' id='password-login' placeholder='Enter this field' label='Mật khẩu'
                                    component={FormControl} />
                                <p className='error_message' ref={errorLogin}></p>
                                <div className='col-12 mt-3 text-center'>
                                    <button type='submit' className='btn btn-outline-primary w-75 mb-5'>Đăng nhập</button>
                                </div>
                            </Form>
                        )
                    }}
                </Formik>
            </div>
            <Formik
                initialValues={initialRegister}
                validationSchema={schemaRegister}
                onSubmit={handleRegister}
            >
                {(props) => {
                    return (
                        <div className='form_register modal fade' id='modal_register' onClick={e => resetForm(props)}>
                            <div className='modal-dialog' onClick={e => e.stopPropagation()}>
                                <Form className='modal-content register-content' onChange={resetMessage}>
                                    <div className='modal-header'>
                                        <button type='reset' className='btn btn-close' data-bs-dismiss='modal'
                                            onClick={e => resetForm(props)}></button>
                                    </div>
                                    <div className='modal-body'>
                                        <div className='row px-5'>
                                            <h2 className='text-center'>Đăng ký</h2>
                                            <Field name='fullName' placeholder='Please enter this field' label='Tên đầy đủ' component={FormControl}/>
                                            <Field name='username' placeholder='Please enter this field' label='Tên đăng nhập' component={FormControl} innerRef={registerName} />
                                            <Field name='password' placeholder='Please enter this field' label='Mật khẩu' component={FormControl} />
                                            <Field name='rePassword' placeholder='Please enter this field' label='Nhập lại mật khẩu' component={FormControl} />
                                            <Field name='avatar' placeholder='Please enter this field' label='Chọn avatar' type='file' component={FormControl} innerRef={registerAvatar} />
                                            <p className='error_message' ref={errorRegister}></p>
                                            <div className='col-12 mt-3 text-center'>
                                                <button type='submit' className='btn btn-outline-primary w-75 mb-5'>Đăng ký</button>
                                            </div>
                                        </div>
                                    </div>
                                </Form>
                            </div>
                        </div>
                    )
                }}
            </Formik>
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
        </div >
    )
}
export default FormLog