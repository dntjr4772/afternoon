import styled from "@emotion/styled";
import React, { useState } from "react";
import ConfirmUpload from "./ConfirmUpload";
import ConfirmUploadSmall from "./ConfirmUploadSmall";
import PhotoUpload from "./PhotoUpload";
import PhotoUploadSmall from "./PhotoUploadSmall";

const Container = styled.div`
  position: relative;
  display: flex;
  background-color: white;
  border-radius: 4px;
  @media only screen and (max-width: 768px) {
    min-height: 540px;
    min-width: 360px;
    height: 95%;
    width: 95%;
  }
  @media only screen and (min-width: 768px) {
    min-height: 720px;
    min-width: 720px;
    height: 90%;
    width: 90%;
  }
  @media only screen and (min-width: 1280px) {
    min-height: 700px;
    min-width: 1050px;
    height: 90%;
    width: 90%;
  }
`;

const Wrapper = styled.div`
  width: 100%;
  height: 100%;
  padding: 15px 20px;
  @media only screen and (max-width: 768px) {
    padding: 10px 10px;
  }
`;

const index = ({ windowWidth, windowHeight }) => {
  const [imageAsFile1, setImageAsFile1] = useState({
    image: null,
    url: "",
    progress: 0,
  });
  const [imageAsFile2, setImageAsFile2] = useState({
    image: null,
    url: "",
    progress: 0,
  });
  const [imageAsFile3, setImageAsFile3] = useState({
    image: null,
    url: "",
    progress: 0,
  });
  const [imageAsFile4, setImageAsFile4] = useState({
    image: null,
    url: "",
    progress: 0,
  });

  const [uploadState, setUploadState] = useState(0);

  return (
    <Container
    // style={{
    //   width: `${windowWidth - 240}px`,
    //   height: `${windowHeight - 80}px`,
    // }}
    >
      <Wrapper>
        {windowWidth > 768 && (
          <PhotoUpload
            {...{
              imageAsFile1,
              setImageAsFile1,
              imageAsFile2,
              setImageAsFile2,
              imageAsFile3,
              setImageAsFile3,
              imageAsFile4,
              setImageAsFile4,
              setUploadState,
              uploadState,
            }}
          ></PhotoUpload>
        )}
        {windowWidth <= 768 && (
          <PhotoUploadSmall
            {...{
              imageAsFile1,
              setImageAsFile1,
              imageAsFile2,
              setImageAsFile2,
              imageAsFile3,
              setImageAsFile3,
              imageAsFile4,
              setImageAsFile4,
              setUploadState,
              uploadState,
            }}
          ></PhotoUploadSmall>
        )}
        {windowWidth > 768 && (
          <ConfirmUpload
            {...{
              imageAsFile1,
              imageAsFile2,
              imageAsFile3,
              imageAsFile4,
              setUploadState,
              uploadState,
              windowWidth,
            }}
          ></ConfirmUpload>
        )}
        {windowWidth <= 768 && (
          <ConfirmUploadSmall
            {...{
              imageAsFile1,
              imageAsFile2,
              imageAsFile3,
              imageAsFile4,
              setUploadState,
              uploadState,
            }}
          ></ConfirmUploadSmall>
        )}
      </Wrapper>
    </Container>
  );
};

export default index;
