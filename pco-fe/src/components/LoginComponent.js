import React, { Component } from "react";
import { I18nProvider } from "../i18nProvider";
import translate from "../i18nProvider/translate";
import { connect } from "react-redux";
import { Link } from "react-router-dom";
import {
  fetchUsersLogin,
  resetValidation,
  fetchLoginGoogle,
  fetchLoginFacebook,
} from "../redux/Authentication/action";
import GoogleLogin from "react-google-login";
import FacebookLogin from "react-facebook-login";
import { setLocales } from "../redux/Language/action";
const validEmailRegex = RegExp(
  /^(([^<>(),;:\s@]+(\.[^<>(),;:\s@"]+)*)|(.+))@(([^<>()[\],;:\s@]+\.)+[^<>()[\],;:\s@]{2,})$/i
);
const validateForm = (errors) => {
  let valid = true;
  Object.values(errors).forEach((val) => val.length > 0 && (valid = false));
  return valid;
};

class Login extends Component {
  constructor(props) {
    super(props);
    this.state = {
      email: "",
      password: "",
      showPassword: false,
      submit: this.props.error,
      errors: {
        email: "",
        password: "",
        login: "",
      },
    };
  }

  handleBlur = (e) => {
    this.setState({
      [e.target.name]: e.target.value,
    });

    const { name, value } = e.target;
    let errors = this.state.errors;
    this.setState({ errors, [name]: value });

    switch (name) {
      case "email":
        errors.email = validEmailRegex.test(value) ? "" : "Email isn't valid!";
        break;

      case "password":
        errors.password =
          value.length < 6
            ? "Password must be at least 6 characters long!"
            : "";
        break;

      default:
        break;
    }
  };
  keyPress = (e) => {
    if (e.key == "Enter") {
      this.handleBlur(e);
    }
  };
  componentDidMount() {
    this.setState({ submit: "" });
  }

  loginWithFacebook = (res) => {
    this.props.fetchLoginFacebook(res.name, res.accessToken);
  };
  loginWithGoogle = (res) => {
    console.log(res);
    this.props.fetchLoginGoogle(res.profileObj.email, res.tokenId);
  };
  keyPress = (e) => {
    if (e.key === "Enter") {
      this.handleBlur(e);
    }
  };
  componentDidMount() {
    this.setState({ submit: "" });
  }

  handleSubmit = (e) => {
    e.preventDefault();

    if (validateForm(this.state.errors)) {
      this.props.fetchUsersLogin({
        email: this.state.email,
        password: this.state.password,
      });
    } else {
      this.setState({
        submit: "Login validation failed, please try again!",
      });
    }
  };

  render() {
    const { errors } = this.state;
    return (
      <div>
        <I18nProvider locale={this.props.locales}>
          <div class="poza"></div>
          <div className="mainPageDivs">
            <h3 className="pageTitle">{translate("log-in")} </h3>
            <div className="error-message-validation">
              {this.props.error
                ? this.props.error
                : this.state.submit
                ? this.state.submit
                : ""}
            </div>
            <p>
              <Link
                to="/register"
                className="reg-log-formLinks"
                style={{ textDecoration: "none" }}
                onClick={this.props.resetValidation}
              >
                {translate("register-")}
              </Link>
            </p>
            <div className="formDiv">
              <form onSubmit={this.handleSubmit}>
                <div>
                  <input
                    type="email"
                    name="email"
                    placeholder="Email"
                    onKeyPress={this.keyPress}
                    onBlur={this.handleBlur}
                    className="Inputs"
                    noValidate
                    required
                  />
                </div>

                {errors.email.length > 0 && (
                  <span className="error">{errors.email}</span>
                )}
                <div className="passwordInputDiv">
                  <input
                    type={this.state.showPassword ? "text" : "password"}
                    placeholder="Password"
                    onBlur={this.handleBlur}
                    name="password"
                    className="passwordInputs"
                    onKeyPress={this.keyPress}
                    noValidate
                    required
                  />

                  <label class="container">
                    <input
                      class="container"
                      type="checkbox"
                      checked={this.state.showPassword}
                      onChange={() =>
                        this.setState({
                          showPassword: !this.state.showPassword,
                        })
                      }
                    />
                    <span class="checkmark"></span>
                  </label>
                </div>

                {errors.password.length > 0 && (
                  <span className="error">{errors.password}</span>
                )}

                <br />
                <button className="btn formButtons" type="submit">
                  {translate("log-in")}
                </button>
              </form>
            </div>

            <div className="routesLinks">
              <Link
                to="/ChangePassword"
                className="reg-log-formLinks"
                style={{ textDecoration: "none" }}
              >
                Forgot your password?
              </Link>
            </div>
            <div>
              <GoogleLogin
                clientId={
                  "758308718543-8aqhjfd3l379i3v9qootvb409u3328oh.apps.googleusercontent.com"
                }
                buttonText={"Login"}
                onSuccess={this.loginWithGoogle}
                cookiePolicy={"single_host_origin"}
              />
            </div>
            <div className="facebook-login-button">
              <FacebookLogin
                appId={"2853364394900683"}
                fields="name,email,picture"
                callback={this.loginWithFacebook}
                icon="fa-facebook"
              />
            </div>
          </div>
        </I18nProvider>
      </div>
    );
  }
}
const mapStateToProps = (state) => {
  return {
    locales: state.locale.lang,
    error: state.users.error,
  };
};

const mapDispatchToProps = (dispatch) => {
  return {
    fetchUsersLogin: (data) => dispatch(fetchUsersLogin(data)),
    setLocale: (lang) => dispatch(setLocales(lang)),
    resetValidation: () => dispatch(resetValidation()),
    fetchLoginGoogle: (token) => dispatch(fetchLoginGoogle(token)),
    fetchLoginFacebook: (token) => dispatch(fetchLoginFacebook(token)),
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(Login);
