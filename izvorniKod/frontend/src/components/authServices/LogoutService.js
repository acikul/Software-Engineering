import axios from 'axios'

function LogoutService(changeUser) {
    changeUser(null)
    axios.defaults.headers.common['Authorization'] = ''
    localStorage.clear()
}

export default LogoutService
