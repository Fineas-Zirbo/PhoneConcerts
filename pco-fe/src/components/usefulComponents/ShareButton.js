import React, { Component } from "react";
// import { Link } from "react-router-dom";
// import Share from "../../images/icon-share.png";
import Facebook from "../../images/icon-facebook.png";
import Twitter from "../../images/icon-twitter.png";
import Instagram from "../../images/icon-instagram.png";
// import { $CombinedState } from "redux";

class ShareButton extends Component {
  constructor(props) {
    super(props);
    this.state = {
      shareOption: false,
    };
  }
  render() {
    return (
      <div>
        {/* <img src={Share} className="share-icon"
        onClick={()=>{this.setState( {shareOption: !this.state.shareOption})}} /> */}

        <button
          onClick={() => {
            this.setState({ shareOption: !this.state.shareOption });
          }}
          className="myAcc-logoutButtons reg-log-formLinks shareButton"
        >
          Share
        </button>
        {this.state.shareOption ? (
          <div className="shareWindow">
            <ul className="icons">
              <li className="shareIcons">
                <a
                  href={`https://www.facebook.com/sharer.php?u=${encodeURI(
                    document.location.href
                  )}`}
                  rel="noreferrer"
                  target="_blank"
                  onClick={() => {
                    this.setState({ shareOption: !this.state.shareOption });
                  }}
                >
                  {" "}
                  <img
                    className="img-responsive image-resize"
                    src={Facebook}
                    alt=""
                  />
                </a>
              </li>
              <li className="shareIcons">
                <a
                  href={`https://www.instagram.com/?url=${encodeURI(
                    document.location.href
                  )}`}
                  rel="noreferrer"
                  target="_blank"
                  onClick={() => {
                    this.setState({ shareOption: !this.state.shareOption });
                  }}
                >
                  {" "}
                  <img
                    className="img-responsive image-resize"
                    src={Instagram}
                    alt=""
                  />
                </a>
              </li>
              <li className="shareIcons">
                <a
                  href={`https://twitter.com/share?url=${encodeURI(
                    document.location.href
                  )}`}
                  rel="noreferrer"
                  target="_blank"
                  onClick={() => {
                    this.setState({ shareOption: !this.state.shareOption });
                  }}
                >
                  {" "}
                  <img
                    className="img-responsive image-resize"
                    src={Twitter}
                    alt=""
                  />
                </a>
              </li>
              <button
                className="x"
                onClick={() => {
                  this.setState({ shareOption: !this.state.shareOption });
                }}
              >
                x
              </button>
            </ul>
          </div>
        ) : null}
      </div>
    );
  }
}

export default ShareButton;
