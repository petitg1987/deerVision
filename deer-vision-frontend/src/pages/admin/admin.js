import React, {Component} from 'react';
import "../pages.css"
import "./admin.css"
import { fetchWithTimeout } from "../../js/request"
import { isJwtExpired } from 'jwt-check-expiration';

//const backendUrl = "https://backend.deervision.studio/";
const backendUrl = "http://127.0.0.1:5000/";

class Admin extends Component {

    constructor(props) {
        super(props);
        this.state = {pwdValue: '', jwtToken: '', logInFail: false, logInFailReason: ''};

        this.handleSubmit = this.handleSubmit.bind(this);
    }

    logIn(jwtToken) {
        localStorage.setItem('token', jwtToken);
        this.setState({logInFail: false, jwtToken: jwtToken});
    }

    isLogIn() {
        let jwtToken = localStorage.getItem('token');
        return jwtToken && jwtToken !== '' && !isJwtExpired(jwtToken);
    }

    logInFail(reason) {
        this.setState({logInFail: true, logInFailReason: reason});
    }

    logOut(event) {
        event.preventDefault();
        localStorage.removeItem('token');
        this.setState({jwtToken: ''});
    }

    async handleSubmit(event) {
        event.preventDefault();
        try {
            let fetchResult = await fetchWithTimeout(backendUrl + 'api/admin/login?password=' + this.state.pwdValue, {method: 'POST'});
            let jsonResult = await fetchResult.json();
            let jwtToken = jsonResult.value;
            if (jwtToken && jwtToken !== '') {
                this.logIn(jwtToken);
            } else {
                this.logInFail("wrong password");
            }
        } catch (e) {
            console.log(e);
            this.logInFail("server unreachable");
        }
    }

    updateInputValue(evt) {
        this.setState({pwdValue: evt.target.value});
    }

    render() {
        let errorLoginMessage = '';
        if (this.state.logInFail) {
            errorLoginMessage = (<div className="errorMessage">Log in fail: {this.state.logInFailReason}</div>)
        }

        if (!this.isLogIn()) {
            return (
                <div>
                    <h2>Admin</h2>
                    <form onSubmit={this.handleSubmit}>
                        <fieldset>
                            {errorLoginMessage}
                            <label htmlFor="password">Password: </label>
                            <input type="password" name="password" id="password" autoCapitalize="none" value={this.state.pwdValue} onChange={evt => this.updateInputValue(evt)}/>
                            <br/>
                            <button type="submit">Log in</button>
                        </fieldset>
                    </form>
                </div>)
        }

        return (
            <div>
                <h2>Admin</h2>
                <div><a href="/" onClick={evt => this.logOut(evt)}>Log out</a></div>
            </div>
        );
    }
}

export default Admin;