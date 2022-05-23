import React from 'react'
import { withRouter } from 'react-router-dom'
import { Link } from 'react-router-dom'
import axios from 'axios'

class UsersList extends React.Component {

    constructor(props) {
        super(props)
        this.state = {
            usersArray: []
        }
    }

    async componentDidMount() {
        let data = [...(await axios.get('/v1/users')).data]
        console.log(data)
        this.setState({
            usersArray: data.map(element => (
                <div key={element.id}>
                    <h3>{element.name}</h3>
                    <h3>{element.surname}</h3>
                    <h3>{element.username}</h3>
                    <Link to={{
                        pathname: '/changeData',
                        state: {
                            id: element.id,
                            name: element.name,
                            surname: element.surname,
                            email: element.email,
                            username: element.username
                        }
                    }}>
                        Promijeni podatke korisnika
                    </Link>
                    <br />
                </div>
            ))
        })
    }

    render() {
        return (
            <div>
                <h1>Popis svih korisničkih računa:</h1>
                {this.state.usersArray}
            </div>
        )
    }
}

export default withRouter(UsersList)