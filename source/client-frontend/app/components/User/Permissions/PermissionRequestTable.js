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
                permission={permission}
                toggleSnack={this.props.toggleSnack}
                putMessageSeen={this.props.putMessageSeen}
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
  putMessageSeen: PropTypes.func,
  toggleSnack: PropTypes.func,
};

export default PermissionRequestTable;
