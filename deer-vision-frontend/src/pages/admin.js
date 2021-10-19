import React, { Component } from 'react';

class Admin extends Component {
    render() {
        return (
            <div>
                <h2>Admin</h2>
                <form name="f" action="/login" method="post">
                    <fieldset>
                        {/*<div th:if="${param.error}" className="alert alert-danger">Invalid username and password.</div>*/}
                        {/*<div th:if="${param.logout}" className="alert alert-success">You have been logged out.</div>*/}

                        <div className="form-group">
                            <label htmlFor="username">Username</label>
                            <input name="username" className="form-control" id="username" autoCapitalize="none"
                                   placeholder="Username" />
                        </div>

                        <div className="form-group">
                            <label htmlFor="password">Password</label>
                            <input type="password" name="password" className="form-control" id="password"
                                   autoCapitalize="none" placeholder="Password" />
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