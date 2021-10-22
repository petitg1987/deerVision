import React, {Component} from 'react';
import "../pages.css"
import "./admin.css"

//const backendUrl = "https://backend.deervision.studio/";
const backendUrl = "http://127.0.0.1:5000/";

class Admin extends Component {

    constructor(props) {
        super(props);
        this.state = {
            pwdValue: '',
            jwtToken: ''
        };
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    async handleSubmit(event) {
        event.preventDefault();
        try {
            let fetchResult = await fetch(backendUrl + 'api/admin/login?password=' + this.state.pwdValue, {method: 'POST'});
            let jsonResult = await fetchResult.json();
            this.setState({
                jwtToken : jsonResult.value
            });
        } catch (e) {
            console.log(e);
        }
    }

    updateInputValue(evt) {
        this.setState({
            pwdValue: evt.target.value
        });
    }

    render() {
        let isLogged = this.state.jwtToken && this.state.jwtToken !== '';
        let renderContent = '';

        if (isLogged) {
            renderContent = <div>You are logged men</div>
        } else {
            renderContent = <form onSubmit={this.handleSubmit}>
                <fieldset>
                    <div className="form-group">
                        <label htmlFor="password">Password: </label>
                        <input type="password" name="password" id="password" autoCapitalize="none" placeholder="Password"
                               value={this.state.pwdValue} onChange={evt => this.updateInputValue(evt)}/>
                    </div>
                    <div className="form-actions">
                        <button type="submit">Log in</button>
                    </div>
                </fieldset>
            </form>
        }

        return (
            <div>
                <h2>Admin</h2>
                {renderContent}
            </div>
        );
    }
}

export default Admin;