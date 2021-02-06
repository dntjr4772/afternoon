import React, { useEffect, useState } from "react";
import styled from "@emotion/styled";
import HeaderLeft from "./HeaderLeft";
import HeaderCenter from "./HeaderCenter";
import HeaderRight from "./HeaderRight";
import { useRouter } from "next/router";
import { useSelector, useDispatch } from "react-redux";
import LoginModal from "../LoginModal";
import SubmitModal from "../SubmitModal";
import { AUTO_LOGIN } from "../../pages/api/user";

const Container = styled.div`
  position: fixed;
  z-index: 10;
  justify-content: center;
  display: flex;
  height: 62px;
  width: 100%;
  font-size: 14px;
  background-color: white;
  box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.08), 0px 0px 1px rgba(1, 0, 0, 0.1);
  transition: all 0.3s;
`;

const Wrapper = styled.div`
  display: flex;
  width: 100%;
  /* max-width: 1280px; */
  height: 100%;
  justify-content: space-between;
  align-items: center;
  padding: 0px 20px;
`;

const ModalFrame = styled.div`
  position: absolute;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  background-color: rgba(0, 0, 0, 0.7);
  z-index: 3;
`;

const CloseBg = styled.div`
  position: absolute;
  width: 100%;
  height: 100%;
  cursor: zoom-out;
`;

const useCounter = () => {
  const isShown = useSelector((state) => state.login.isShown);
  const loginState = useSelector((state) => state.login.loginState);
  const autoLogin = useSelector((state) => state.login.autoLogin);
  const submitShown = useSelector((state) => state.submit.submitShown);

  const dispatch = useDispatch();
  const toggle = async () => {
    await dispatch({ type: "TOGGLE" });
  };
  const autoLoginCheck = async () => {
    await dispatch({ type: "AUTO_LOGIN_CHECK" });
  };
  const loginStateTrue = async () => {
    await dispatch({ type: "LOGIN_STATE_TRUE" });
  };
  const loginStateFalse = async () => {
    await dispatch({ type: "LOGIN_STATE_FALSE" });
  };
  const toggleSubmit = async () => {
    await dispatch({ type: "TOGGLE_SUBMIT" });
  };

  return {
    autoLoginCheck,
    toggleSubmit,
    submitShown,
    loginState,
    loginStateTrue,
    loginStateFalse,
    autoLogin,
    isShown,
    toggle,
  };
};

const index = () => {
  const router = useRouter();
  const routerPath = router.pathname;
  const [inputFocus, setInputFocus] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");

  const {
    autoLoginCheck,
    toggleSubmit,
    submitShown,
    loginStateTrue,
    loginStateFalse,
    loginState,
    isShown,
    autoLogin,
    toggle,
  } = useCounter();

  const [windowWidth, setWindowWidth] = useState<number>();
  const [windowHeight, setWindowHeight] = useState<number>();

  useEffect(() => {
    autoLoginCheck();

    const doAutoLogin = async () => {
      const result = await AUTO_LOGIN();
      console.log(result);
      if (result.status === 200) {
        loginStateTrue();
      } else {
        loginStateFalse();
      }
    };

    // console.log(autoLogin, loginState);
    if (autoLogin) {
      if (!loginState) {
        doAutoLogin();
      }
    }

    setWindowWidth(window.innerWidth);
    setWindowHeight(window.innerHeight);

    const resizeHandler = () => {
      setWindowWidth(window.innerWidth);
      setWindowHeight(window.innerHeight);
    };

    window.addEventListener("resize", resizeHandler);

    const cleanup = () => {
      window.removeEventListener("resize", resizeHandler);
    };

    return cleanup;
  });

  useEffect(() => {
    document.body.style.overflow =
      isShown || submitShown || inputFocus ? "hidden" : "scroll";
  }, [isShown, submitShown, inputFocus]);

  const containerStyle = {
    display: routerPath === "/signup" ? "none" : "flex",
    boxShadow:
      (routerPath === "/" || routerPath === "/home") && !inputFocus
        ? "none"
        : "0px 4px 12px rgba(0, 0, 0, 0.08), 0px 0px 1px rgba(1, 0, 0, 0.1)",
    backgroundColor:
      (routerPath === "/" || routerPath === "/home") && !inputFocus
        ? "rgba(255,255,255,0)"
        : "rgba(255,255,255,1)",
  };

  const onClickSubmitBg = () => {
    if (
      confirm(
        "PINSET : 사진 등록을 취소하시겠습니까?\n확인을 누르시면 현재까지 작업이 사라집니다."
      )
    ) {
      toggleSubmit();
    } else {
      return;
    }
  };

  return (
    <>
      {inputFocus && (
        <ModalFrame
          onClick={() => {
            setInputFocus(false);
          }}
          style={{ position: "fixed", height: windowHeight }}
        ></ModalFrame>
      )}
      <Container style={containerStyle}>
        {isShown && (
          <>
            <ModalFrame style={{ height: windowHeight }}>
              <CloseBg onClick={toggle}></CloseBg>
              <LoginModal></LoginModal>
            </ModalFrame>
          </>
        )}
        {submitShown && (
          <>
            <ModalFrame style={{ height: windowHeight }}>
              <CloseBg onClick={onClickSubmitBg}></CloseBg>
              <SubmitModal
                windowWidth={windowWidth}
                windowHeight={windowHeight}
              ></SubmitModal>
            </ModalFrame>
          </>
        )}
        <Wrapper>
          <HeaderLeft
            router={router}
            routerPath={routerPath}
            setInputFocus={setInputFocus}
            inputFocus={inputFocus}
            searchTerm={searchTerm}
            setSearchTerm={setSearchTerm}
          />
          <HeaderCenter
            routerPath={routerPath}
            inputFocus={inputFocus}
            setInputFocus={setInputFocus}
          />
          <HeaderRight
            router={router}
            routerPath={routerPath}
            inputFocus={inputFocus}
            setInputFocus={setInputFocus}
          />
        </Wrapper>
      </Container>
    </>
  );
};

export default index;
