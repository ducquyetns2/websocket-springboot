import { Navigate, Route, Routes } from 'react-router-dom'
import { useSelector } from 'react-redux'
import Navigation from './components/navigation'
import HomePage from './pages/homePage'
import FormLog from './pages/login'
import Messenger from './pages/messenger'
import { routes } from './store/constant'

function App() {
  const accessToken = useSelector(state => 
    {
      console.log("User :",state.user)
      return state.user.accessToken
    }
    )
  return (
    <div className="App">
      <Routes>
        <Route path={routes.login} element={<FormLog />}></Route>
        <Route path={routes.home} element={<Navigation />}>
          <Route index element={(accessToken) ? <HomePage /> : <Navigate to='/login' />}></Route>
          <Route path={routes.messenger} element={(accessToken) ? <Messenger /> : <Navigate to='/login' />}></Route>
        </Route>
      </Routes>
    </div>
  )
}

export default App;
