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
            {this.props.closures.length > 0 && this.props.closures.map((item, index) => (
              <div>
                <p>{item.description}</p>
                <FormControlLabel
                  control={
                    <Switch
                      checked={item.approved}
                      onChange={() => this.props.changeClosure(item, index)}
                    />
                  }
                  label={item.approved ? 'Yes' : 'No'}
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
  changeClosure: PropTypes.func,
};

export default ClosureSwitch;
