import React from 'react'
import './UserTesting.css'

class UserTesting extends React.Component {

    render() {
        return (
            <div className="tablica">
                <table>
                    <tr>
                        <th>Username</th>
                        <th>Password</th>
                    </tr>
                    <tr>
                        <td>admin</td>
                        <td>admin</td>
                    </tr>
                    <tr>
                        <td>athlete</td>
                        <td>athlete</td>
                    </tr>
                    <tr>
                        <td>athlete1</td>
                        <td>athlete1</td>
                    </tr>
                    <tr>
                        <td>athlete2</td>
                        <td>athlete2</td>
                    </tr>
                    <tr>
                        <td>athlete3</td>
                        <td>athlete3</td>
                    </tr>
                    <tr>
                        <td>coach</td>
                        <td>coach</td>
                    </tr>
                    <tr>
                        <td>renter</td>
                        <td>renter</td>
                    </tr>
                </table>
            </div>
        )
    }
}

export default UserTesting