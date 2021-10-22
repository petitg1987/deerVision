import React, {Component} from 'react';
import './usage-graph.css';
import {getWithToken} from "../../js/request";

class UsageGraph extends Component {

    constructor(props) {
        super(props);
        this.state = {usageJson: '{}'};
    }

    async componentDidMount() {
        let result = await getWithToken(this.props.backendUrl + 'api/admin/usage', this.props.token);
        this.setState({usageJson: result});
    }

    render() {
        return (
            <div>{this.state.usageJson}</div>
        );
    }
}

export default UsageGraph;