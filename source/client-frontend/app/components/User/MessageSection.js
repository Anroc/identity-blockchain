import React from 'react';
import PropTypes from 'prop-types';
import Button from 'material-ui/Button';

class MessageSection extends React.Component {
  componentDidMount() {
  }
  render() {
    return (
      <section>
        <Button
          onClick={this.props.getMessages}
        >
          Get Messages
        </Button>
        <div>
          {this.props.messages && JSON.stringify(this.props.messages)}
        </div>
      </section>
    );
  }
}

MessageSection.propTypes = {
  getMessages: PropTypes.func,
  messages: PropTypes.array,
};

export default MessageSection;
