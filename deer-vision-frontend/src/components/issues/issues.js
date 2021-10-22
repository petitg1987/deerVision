import React, {Component} from 'react';
import './issues.css';
import {getWithToken} from "../../js/request";

class Issues extends Component {

    constructor(props) {
        super(props);
        this.state = {tableData: []};
    }

    async componentDidMount() {
        let issuesJson = await getWithToken(this.props.backendUrl + 'api/admin/issues', this.props.token);

        let issuesData = [];
        issuesJson.forEach(issue => {
            let appName = issue.appId
                .replace(/([A-Z])/g, ' $1') //insert a space before all caps
                .replace(/^./, (str) => str.toUpperCase());
            let shortAppVersion = issue.appVersion
                .replace('snapshot', 'snap');
            let osName = issue.operatingSystem
                .replace('-', ' ')
                .replace(/^./, (str) => str.toUpperCase());
            let shortUserKey = parseInt(issue.userKey
                .replace(/-.*$/, ''))
                .toString(36)
                .toUpperCase()

            issuesData.push(
                <tr key={issue.id}>
                    <td>{issue.dateTime}</td>
                    <td>{appName}</td>
                    <td>{shortAppVersion}</td>
                    <td>{osName}</td>
                    <td>{shortUserKey}</td>
                    <td>View | Delete</td>
                </tr>
            );
        })

        this.setState({tableData: issuesData});

        console.log(issuesJson);
    }

    render() {
        return (
            <div>
                <table>
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>App name</th>
                            <th>App version</th>
                            <th>OS</th>
                            <th>User key</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        {this.state.tableData}
                    </tbody>
                </table>
            </div>
        );
    }
}

export default Issues;