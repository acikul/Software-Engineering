import axios from 'axios'
import LoginService from './LoginService'
//registers user, takes object with required data as parameter,
// returns nothing


async function RegisterService(registerData, changeUser) {
    let loginData = {
        username: registerData.username,
        password: registerData.password
    }
    try {
        await axios.post('auth/signup', registerData)
        await LoginService(loginData, changeUser)
        alert('Uspješna registracija!')
    }
    catch (err) {
        console.log(err)
        alert('Pogreška u registraciji')
    }
}

export default RegisterService
