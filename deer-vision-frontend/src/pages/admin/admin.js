import React, {Component} from 'react';
import "../pages.css"

class Admin extends Component {

    async handleSubmit(event) {
        event.preventDefault();
        try {
            let fetchResult = await fetch('http://localhost:5000/api/admin/login?password=dev', {method: 'POST'}); //TODO use password
            let jsonResult = await fetchResult.json();
            alert('Token: ' + jsonResult.value);
        } catch (e) {
            console.log(e); //TODO display error
        }
    }

    render() {
        return (
            <div>
                <h2>Admin</h2>
                <form onSubmit={this.handleSubmit}>
                    <fieldset>
                        <div className="form-group">
                            <label htmlFor="password">Password</label>
                            <input type="password" name="password" className="form-control" id="password" autoCapitalize="none" placeholder="Password"/>
                        </div>

                        <div className="form-actions">
                            <button type="submit" className="btn btn-primary">Log in</button>
                        </div>
                    </fieldset>
                </form>
            </div>
        );
    }
}

export default Admin;