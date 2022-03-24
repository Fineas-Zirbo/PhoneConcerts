import {
  LOGIN_REQUEST,
  LOGIN_SUCCES,
  LOGIN_ERROR,
  EDIT_PROFILE,
  CONCERT_LIST,
  RESET_VALIDATION
} from "./actionType";

export const fetchUsersLogin = (data) => {
  return (dispatch) => {
    fetch("/api/user/login", {
      method: "POST",
      headers: { "Content-Type": `application/json` },
      body: JSON.stringify(data),
      credentials: "include",
    })
      .then((res) =>{   
      return res.json()
      })
      .then((res) => {
        if(res.success === false){ 
          throw new Error('Email or password incorect!');
        }       
        dispatch(loginSuccess({ data: res.user, success: res.success, message:res.message }));
      })
      .catch((err) => {
        console.log(err)
        dispatch(loginError("Login Failed, Please Try Again"));
      });
  };
};


export const fetchUsersRegister = (data) => {
  return (dispatch) => {
    fetch(`/api/user/register`, {
      method: "POST",
      body: JSON.stringify(data),
      headers: { "Content-Type": "application/json" },
      credentials: "include",
    })
      .then((res) => res.json())
      .then((res) => {
        if(res.success === false){ 
          throw new Error('Your data is invalid!');
        }      
        dispatch(loginSuccess({ data: res.user, message: res.message, succes:res.success}));
      })

      .catch((err) => {
        dispatch(loginError("This account already exists!"));
        console.error(err.value);
      });
  };
};

export const fetchConcerts = () => {
  return (dispatch) => {
    fetch(`/api/user/concerts`, {
      method: "GET",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
    })
      .then((res) => res.json())
      .then((res) => {   

        dispatch(concertList({ list: res.concerts}));
      })

      .catch((err) => {
        dispatch(loginError(err));
        console.error(err.value);
      });
  };
};



export const verifyRefreshToken = () => {
  return (dispatch) => {
    fetch(`/api/user/isAuthenticated`, {
      mothod: "GET",
      headers: { "Content-Type": "application/json","Cookie":"jwt=super_super_jwt; " },
      credentials: "include",
    })
      .then((res) => res.json())
      .then((res) => {
        if (!res.isAuthenticated) {
          throw new Error('You are not authenticated!');
        }

        if (res.isAuthenticated) {
          dispatch(loginSuccess({ data: res.user}));
        }
        if(window.location.pathname === "/account"){
          dispatch(loginRequest())
        }
      })
      .catch((err) => {
        console.log(err)
        dispatch(loginError(""));
      });
  };
};

export const logoutUser = () => {
  return (dispatch) => {
    fetch("api/user/logout", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
    })
      .then((res) => {
        if (res.status === 200) {
          dispatch(loginError());
        }
      })
      .catch((err) => {
        dispatch(loginError(err));
      });
  };
};

export const fetchUsersEditProfile = ({firstName,lastName,email,phone}) => {

  return (dispatch) => {

    fetch(`/api/user/editAccount`, {
      method: "GET",
      body: JSON.stringify(firstName,lastName,email,phone),
      headers: {
        "Content-Type": "application/json",
    
      },
      credentials: "include",
    })
      .then((res) => res.json())
      .then((res) => {
        if(res.isAuthenticated){}
        dispatch(loginSuccess({ data: res.user }));

   
        dispatch(editProfile());
      })
      .catch((err) => {

        dispatch(loginError(err));
      });
  };
};

export const changePassword = (data) => {
  return (dispatch) => {
    fetch(`/api/user/ChangePassword`, {
      method: "POST",
      body: JSON.stringify(data),
      headers: { "Content-Type": "application/json" },
      credentials: "include",
    })
      .then((res) => res.json())
      .then((res) => {
        dispatch(loginError());
      })
      .catch((err) => {
        dispatch(loginError(err));
      });
  };
};
export const fetchLoginGoogle = (email,tokenId) => {
  return (dispatch) => {
    fetch("/api/user/auth/google", {
      method: "POST",
      headers: { "Content-Type": `application/json` },
      body: JSON.stringify(email,tokenId),
      credentials: "include",
    })
      .then((res) =>{   
      return res.json()
      })
      .then((res) => {
        if(res.success === false || res.message === ""){ 
          throw new Error('Email or password incorect!');
        }       
        dispatch(loginSuccess({ data: res.user, success: res.success, message:res.message }));
      })
      .catch((err) => {
        console.log(err)
        dispatch(loginError("Login Failed, Please Try Again"));
      });
  };
};
export const fetchLoginFacebook = (tokenId) => {
  return (dispatch) => {
    fetch("/api/user/auth/facebook", {
      method: "POST",
      headers: { "Content-Type": `application/json` },
      body: JSON.stringify(tokenId),
      credentials: "include",
    })
      .then((res) =>{   
      return res.json()
      })
      .then((res) => {
        if(res.success === false || res.message === ""){ 
          throw new Error('Email or password incorect!');
        }
        dispatch(loginSuccess({ data: res.user, success: res.success, message:res.message }))
             
       })
      .catch((err) => {
        console.log(err)
        dispatch(loginError("Login Failed, Please Try Again"));
      });
  };
};



export const AccountRequest = (email) => {
  return (dispatch) => {
      fetch(`/api/user/account?email=${email}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
    })
      .then((res) =>{  
    if(res.status>399){
     throw new Error('my error');
    }
       return res.json()
      })
      .then((res) => {
        dispatch(loginSuccess({ data: res }));

   
      })
      .catch((err) => {

      console.log(err.message)
        dispatch(loginError(err));
      });
  };
};

export const loginRequest = () => {
  return {
    type: LOGIN_REQUEST,
  };
};

export const loginSuccess = (res) => {
  return {
    type: LOGIN_SUCCES,
    payload: res,
  };
};

export const loginError = (err) => {
  return {
    type: LOGIN_ERROR,
    payload: err,
  };
};

export const editProfile = () => {
  return {
    type: EDIT_PROFILE,
  };
};

export const concertList = (list) => {
  return {
    type: CONCERT_LIST,
    payload:list, 
  }
}
export const resetValidation=()=>{
  return{
    type:RESET_VALIDATION
  }
}
