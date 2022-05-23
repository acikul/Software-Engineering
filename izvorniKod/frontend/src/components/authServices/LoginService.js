import axios from 'axios'
import DataService from './DataService'

//logs user in, takes object with username and password as
//parameter, returns nothing

async function LoginService(loginData, changeUser) {
    try {
        let response = await axios.post('auth/signin', loginData)
        localStorage.setItem('token', response.data.accessToken)
        axios.defaults.headers.common['Authorization'] = 'Bearer ' + response.data.accessToken
        let userData = await DataService()
        changeUser(userData)
        console.log(userData)
        alert('Uspješna prijava!')
    }
    catch (err) {
        console.log(err)
        alert('Pogreška u prijavi!')
    }
}

export default LoginService
