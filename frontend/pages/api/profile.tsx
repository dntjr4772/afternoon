import axios from "axios";
import secrets from "../../secrets";

const API_ROOT_URI = secrets.API_ROOT_URI;
const VIA_API_DEV = secrets.VIA_API_DEV;

export const GET_MY_INFO = async (req) => {
  console.log(req);
  await axios.get(API_ROOT_URI + "/accounts/" + req.accountId).then((res) => {
    console.log(res.data);
  });
};

export const ADD_TAGS = async (req) => {
  const authToken = window.localStorage.getItem("authToken");
  if (!VIA_API_DEV) {
    try {
      console.log(req);
      return { status: 200, data: {} };
    } catch (error) {
      console.log(error);
    }
    return { status: false };
  } else {
    // API 요청 시 실행
    let status;
    let data;

    // GET : 단순 데이터 조회 -> req. x
    // POST : 데이터 생성용 ,create -> req. o
    // PUT : update 같은 데이터 변경용 -> req. o
    // DELETE : delete같읕 아이디 삭제용 -> req. x

    try {
      await axios
        .put(API_ROOT_URI + "/api/accounts/mytag", req, {
          headers: { Authorization: `Bearer ${authToken}` },
        })
        .then((res) => {
          console.log(res);
          status = res.status;
          data = res.data;
        });
      return { status, data };
    } catch (error) {
      console.log(error);
    }
    return { status: false };
  }


};

export const DELETE_TAGS = async () => {
  const authToken = window.localStorage.getItem("authToken");
  
  console.log(authToken);
  
};
