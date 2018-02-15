import React from 'react';
import PropTypes from 'prop-types';
import Paper from 'material-ui/Paper';
import { FormGroup } from 'material-ui/Form';

import PermissionForm from '../PermissionForm';

class PermissionRequestTable extends React.Component {

  constructor() {
    super();
    this.state = {
      checkedA: false,
    };

  }
  render() {
    return (
      <Paper className="">
        <FormGroup>
          {this.props.permissions.map((permission) => (
            <Paper key={permission.id}>

              <PermissionForm
                handleChange={this.props.handleChange}
                value={this.props.value}
                sendPermissionAnswer={this.props.sendPermissionAnswer}
                permission={permission}
              />
            </Paper>
          ), 0)}
        </FormGroup>
      </Paper>
    );
  }
}

PermissionRequestTable.propTypes = {
  permissions: PropTypes.array,
  handleChange: PropTypes.func,
  value: PropTypes.string,
  sendPermissionAnswer: PropTypes.func,
};

export default PermissionRequestTable;
