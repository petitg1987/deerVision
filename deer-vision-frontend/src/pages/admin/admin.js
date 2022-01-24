import React, {Component} from 'react';
import "../pages.css"
import "./admin.css"
import {postRequest} from "../../js/request"
import {isJwtExpired} from 'jwt-check-expiration';
import UsageGraph from "../../components/usage-graph/usage-graph";
import Issues from "../../components/issues/issues";

const backendUrl = "https://backend.deervision.studio/";
//const backendUrl = "http://127.0.0.1:5000/";

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

    getToken() {
        return localStorage.getItem('token');
    }

    isLogIn() {
        let jwtToken = this.getToken();
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
            let jsonResult = await postRequest(backendUrl + 'api/admin/login?password=' + this.state.pwdValue);
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
            errorLoginMessage = (<div className="error-message">Log in fail: {this.state.logInFailReason}</div>)
        }

        if (!this.isLogIn()) {
            return (
                <div>
                    <h2>Log-in</h2>
                    <div className="vertical-spacer"/>
                    <div className="horizontal-spacer"/>
                    <form onSubmit={this.handleSubmit}>
                        <fieldset>
                            {errorLoginMessage}
                            <label htmlFor="password">Password: </label>
                            <input type="password" name="password" id="password" autoCapitalize="none" value={this.state.pwdValue} onChange={evt => this.updateInputValue(evt)}/>
                            <br/><br/>
                            <button type="submit">Log in</button>
                        </fieldset>
                    </form>
                </div>)
        }

        return (
            <div>
                <h2>Usages</h2>
                <div className="usage-container">
                    <div className="vertical-spacer"/>
                    <div className="horizontal-spacer"/>
                    <UsageGraph backendUrl={backendUrl} token={this.getToken()}/>
                </div>

                <div className="vertical-spacer"/>
                <h2>Issues</h2>
                <div className="issues-container">
                    <div className="vertical-spacer"/>
                    <div className="horizontal-spacer"/>
                    <Issues backendUrl={backendUrl} token={this.getToken()}/>
                </div>

                <div className="logout-container">
                    <div className="vertical-spacer"/>
                    <small>
                        <a className="text-link" href="/" onClick={evt => this.logOut(evt)}>Log out</a>
                    </small>
                </div>
            </div>
        );
    }
}

export default Admin;
