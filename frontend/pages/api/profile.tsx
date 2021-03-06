import axios from "axios";
import secrets from "../../secrets";
import { GetMyInfoData } from "../../data/ApiData";

const API_ROOT_URI = secrets.API_ROOT_URI;
const VIA_API_DEV = secrets.VIA_API_DEV;

const timeout = (ms) => {
  return new Promise((resolve) => setTimeout(resolve, ms));
};

export const GET_MY_INFO = async (config) => {
  //replace_console_log("GET_MY_INFO : REQUEST");

  let status: number = 0;
  let data: {
    accountId: number;
    accountName: string;
    accountEmail: string;
    accountNickname: string;
    accountCreateDate: string;
    accountBio: string;
    accountPhoto: string;
    following?: Array<{ id: number; name: string; nickname: string }>;
    follower?: Array<{ id: number; name: string; nickname: string }>;
    accountFollowingCnt: number;
    accountFollowerCnt: number;
    writtenPosts?: Array<{
      postsId: number;
      postsWriter: string;
      postsTitle: string;
      postsPhoto: string;
    }>;
    writtenPostsCnt: number;
    likePosts?: Array<{
      postsId: number;
      postsWriter: string;
      postsTitle: string;
      postsPhoto: string;
    }>;
    likePostsCnt: number;
    tags?: Array<{ tagId: number }>;
  } = {
    accountId: 0,
    accountName: "",
    accountNickname: "",
    accountEmail: "",
    accountCreateDate: "",
    accountBio: "",
    accountPhoto: "",
    following: [],
    follower: [],
    accountFollowingCnt: 0,
    accountFollowerCnt: 0,
    writtenPosts: [],
    writtenPostsCnt: 0,
    likePosts: [],
    likePostsCnt: 0,
    tags: [],
  };

  if (!VIA_API_DEV) {
    //replace_console_log("GET_MY_INFO : LOCAL");

    try {
      await timeout(1000);
      // throw new Error();
      status = 200;
      data = GetMyInfoData.data;
    } catch (error) {
      //replace_console_log(error);
    }
  } else {
    //replace_console_log("GET_MY_INFO : DEV");

    try {
      await axios.get(API_ROOT_URI + "/api/accounts", config).then((res) => {
        status = res.status;
        data = res.data.data;
      });
    } catch (error) {
      //replace_console_log(error);
    }
  }

  return { status, data };
};

export const ADD_TAGS = async (req, config) => {
  //replace_console_log("ADD_TAGS : REQUEST");
  //replace_console_log(req);

  let status: number = 0;
  let data: {} = {};

  if (!VIA_API_DEV) {
    //replace_console_log("ADD_TAGS : LOCAL");

    try {
      await timeout(1000);
      // throw new Error();
      status = 200;
      data = {};
    } catch (error) {
      //replace_console_log(error);
    }
  } else {
    //replace_console_log("ADD_TAGS : DEV");

    try {
      await axios
        .put(API_ROOT_URI + "/api/accounts/mytag", req, config)
        .then((res) => {
          status = res.status;
          data = res.data.data;
        });
    } catch (error) {
      //replace_console_log(error);
    }
  }

  return { status, data };
};

export const DELETE_TAGS = async (req, config) => {
  //replace_console_log("DELETE_TAGS : REQUEST => " + req);

  let status: number = 0;
  let data: {} = {};

  if (!VIA_API_DEV) {
    //replace_console_log("DELETE_TAGS : LOCAL");

    try {
      await timeout(1000);
      // throw new Error();
      status = 200;
      data = {};
    } catch (error) {
      //replace_console_log(error);
    }
  } else {
    //replace_console_log("DELETE_TAGS : DEV");

    try {
      await axios
        .delete(API_ROOT_URI + `/api/accounts/mytag/${req}`, config)
        .then((res) => {
          status = res.status;
          data = res.data.data;
        });
    } catch (error) {
      //replace_console_log(error);
    }
  }

  return { status, data };
};

export const FOLLOW_USER = async (req, config) => {
  //replace_console_log("FOLLOW_USER : REQUEST => " + req);

  let status: number = 0;
  let data: {} = {};

  if (!VIA_API_DEV) {
    //replace_console_log("FOLLOW_USER : LOCAL");

    try {
      await timeout(1000);
      // throw new Error();
      status = 200;
      data = {};
    } catch (error) {
      //replace_console_log(error);
    }
  } else {
    //replace_console_log("FOLLOW_USER : DEV");

    try {
      await axios
        .put(API_ROOT_URI + "/api/accounts/myfollowing", req, config)
        .then((res) => {
          status = res.status;
          data = res.data.data;
        });
    } catch (error) {
      //replace_console_log(error);
    }
  }

  return { status, data };
};

export const UNFOLLOW_USER = async (req, config) => {
  //replace_console_log("UNFOLLOW_USER : REQUEST => " + req);

  let status: number = 0;
  let data: {} = {};

  if (!VIA_API_DEV) {
    //replace_console_log("UNFOLLOW_USER : LOCAL");

    try {
      await timeout(1000);
      // throw new Error();
      status = 200;
      data = {};
    } catch (error) {
      //replace_console_log(error);
    }
  } else {
    //replace_console_log("UNFOLLOW_USER : DEV");

    try {
      await axios
        .delete(API_ROOT_URI + `/api/accounts/myfollowing/${req}`, config)
        .then((res) => {
          status = res.status;
          data = res.data.data;
        });
    } catch (error) {
      //replace_console_log(error);
    }
  }

  return { status, data };
};

export const EDIT_PROFILE = async (req, config) => {
  //replace_console_log("EDIT_PROFILE : REQUEST");
  //replace_console_log(req);

  let status: number = 0;
  let data: {} = {};

  if (!VIA_API_DEV) {
    //replace_console_log("EDIT_PROFILE : LOCAL");

    try {
      await timeout(1000);
      // throw new Error();
      status = 200;
      data = {};
    } catch (error) {
      //replace_console_log(error);
    }
  } else {
    //replace_console_log("EDIT_PROFILE : DEV");

    try {
      await axios
        .put(API_ROOT_URI + `/api/accounts`, req, config)
        .then((res) => {
          status = res.status;
          data = res.data.data;
        });
    } catch (error) {
      //replace_console_log(error);
    }
  }

  return { status, data };
};
