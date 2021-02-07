import React from "react";
import styled from "@emotion/styled";
import Button from "../Button";
import color from "../../styles/theme";
import Image from "next/image";

const Container = styled.div`
  display: flex;
  width: 200px;
  background-color: white;
  border: 2px solid black;
`;

const Wrapper1 = styled.div`
  display: flex;
  position: relative;
  width: 300px;
  height: 60px;
`;

const ImgBox = styled.div`
  display: flex;
  position: relative;
  width: 300px;
  height: 60px;
  /* margin: 30px 0px 10px 0px; */
`;

const Wrapper2 = styled.div`
  display: flex;
  /* position: relative; */
  width: 700px;
  height: 60px;
`;

const UserName = styled.div``;

const PinTag = styled.div``;

const UserLink = styled.div``;

const UserComment = styled.div``;

const CommentText = styled.div``;

const index = ({ userData }) => {
  const { userImg, userName } = userData;

  return (
    <Container>
      <Wrapper1>
        <ImgBox>
          <Image
            src={userImg}
            class="next_border_image circle"
            layout="fill"
            objectFit="contain"
          ></Image>
        </ImgBox>
      </Wrapper1>
      <Wrapper2>
        <UserName>{userName}</UserName>
        <PinTag>
          <Button></Button>
        </PinTag>
        <UserLink>
          <Button></Button>
        </UserLink>
        <UserComment></UserComment>
      </Wrapper2>
    </Container>
  );
};

export default index;
