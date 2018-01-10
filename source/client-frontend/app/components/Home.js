import React, {Component} from 'react'
import {connect} from 'react-redux'

class Home extends Component {
  render () {
    return (
      <article>
        <div>
          <section className='text-section'>
            <h1>Welcome to the IOSL Blockchain Identity project!</h1>
            <p>This application demonstrates what a blockchain-based approach to digital identity might look like.</p>
            <p>You may currently log in, register or go to the dashboard</p>
          </section>
        </div>
      </article>
    )
  }
}
/*
          <section className='text-section'>
            <h2>Authentication</h2>
            <p>Authentication happens in <code>app/auth/index.js</code>, using <code>fakeRequest.js</code> and <code>fakeServer.js</code>. <code>fakeRequest</code> is a fake <code>XMLHttpRequest</code> wrapper. <code>fakeServer</code> responds to the fake HTTP requests and pretends to be a real server, storing the current users in local storage with the passwords encrypted using <code>bcrypt</code>.</p>
          </section>
 */

function select (state) {
  return {
    data: state
  }
}

export default connect(select)(Home)
