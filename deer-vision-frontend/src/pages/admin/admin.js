import React, {Component} from 'react';
import "../pages.css"
import "./admin.css"
import {postRequest} from "../../js/request"
import {isJwtExpired} from 'jwt-check-expiration';
import UsageGraph from "../../components/usage-graph/usage-graph";
import Issues from "../../components/issues/issues";
import ActionCompletionTimeGraph from "../../components/action-completion-time-graph/action-completion-time-graph";
import TotalPlayer from "../../components/total-player/total-player";
import VisitorCountryGraph from "../../components/visitor-graph/visitor-country-graph";
import VisitorCountGraph from "../../components/visitor-graph/visitor-count-graph";
import {getBackendUrl} from "../../js/access";
import LastActionCompletion from "../../components/last-action-completion-time/last-action-completion";

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
            let jsonResult = await postRequest(getBackendUrl() + 'api/admin/login?password=' + this.state.pwdValue);
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
                    <div className="admin-container">
                        <form onSubmit={this.handleSubmit}>
                            <fieldset>
                                {errorLoginMessage}
                                <label htmlFor="password">Password: </label>
                                <input type="password" name="password" id="password" autoCapitalize="none" value={this.state.pwdValue} onChange={evt => this.updateInputValue(evt)}/>
                                <br/><br/>
                                <button type="submit">Log in</button>
                                <br/><br/>
                            </fieldset>
                        </form>
                    </div>
                </div>)
        }

        return (
            <div>
                <h2>All games</h2>
                <div className="admin-container">
                    <div className="sub-title">Players</div>
                    <div className="admin-info-container">
                        <TotalPlayer token={this.getToken()}/>
                    </div>

                    <div className="sub-title">Usage</div>
                    <div className="admin-info-container">
                        <UsageGraph token={this.getToken()}/>
                    </div>

                    <div className="sub-title">Issues</div>
                    <div className="admin-info-container">
                        <Issues token={this.getToken()}/>
                    </div>
                </div>

                <h2>Photon Engineer</h2>
                <div className="admin-container">
                    <div className="sub-title">Action Completion Time</div>
                    <div className="admin-info-container">
                        <ActionCompletionTimeGraph appId='photonEngineer' token={this.getToken()}/>
                    </div>
                </div>
                <br/>
                <div className="admin-container">
                    <div className="sub-title">Last Action Completion By User</div>
                    <div className="admin-info-container">
                        <LastActionCompletion appId='photonEngineer' token={this.getToken()}/>
                    </div>
                </div>

                <h2>Website</h2>
                <div className="admin-container">
                    <div className="sub-title">Visitor counter</div>
                    <div className="admin-info-container">
                        <VisitorCountGraph token={this.getToken()}/>
                    </div>

                    <div className="sub-title">Visitor's country</div>
                    <div className="admin-info-container">
                        <VisitorCountryGraph token={this.getToken()}/>
                    </div>
                </div>

                <div className="logout-container">
                    <small>
                        <a className="text-link" href="/" onClick={evt => this.logOut(evt)}>Log out</a>
                    </small>
                </div>
            </div>
        );
    }
}

export default Admin;
