import React from 'react';
import Switch from 'material-ui/Switch';
import { FormControlLabel } from 'material-ui/Form';
import PropTypes from 'prop-types';

class ClosureSwitch extends React.Component {

  constructor() {
    super();
    this.state = {
      showClosure: false,
    };
  }

  componentDidMount() {
    this.props.closures.forEach((item) => {
      this.setState({
        [item.claimID]: item.approved,
      });
    });
    this.isClaimEmpty();
  }

  isClaimEmpty() {
    if (this.props.closures !== []) {
      this.setState({
        showClosure: true,
      });
    }
  }

  render() {
    return (
      <div>
        { this.state.showClosure ? (
          <div>
            {this.props.closures.length > 0 && this.props.closures.map((item) => (
              <div>
                <p>{item.description}</p>
                <FormControlLabel
                  control={
                    <Switch
                      checked={this.state[item.claimID]}
                      onChange={() => this.setState({ [item.claimID]: !this.state[item.claimID] })}
                    />
                  }
                  label={this.state[item.claimID] ? 'Yes' : 'No'}
                />
              </div>
            ), 0)}
          </div>
        ) : null}
      </div>
    );
  }
}

ClosureSwitch.propTypes = {
  closures: PropTypes.array,
};

export default ClosureSwitch;
