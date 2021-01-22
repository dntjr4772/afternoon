import React, { useEffect, useState } from "react";
import styled from "@emotion/styled";
import Modal from "../../components/LoginModal";
import Image from "next/image";
// import Button from "../../components/Buttons"

const Container = styled.div`
  position: relative;
  /* margin-top: 80px; */
  height: 100%;
  background-color: #eeeeee;
  display: flex;
  justify-content: center;
  align-items: center;
`;

const TextBox1 = styled.div`
	display: flex;
	text-align: left;
	font-size: 48px;
	font-weight: bold; 
`;

const TextBox2 = styled.div`
	display: flex;
	text-align: left;
	font-size: 24px;
	margin-top: 40px;
`;

const TextBox3 = styled.div`
	display: flex;
	text-align: left;
	font-size: 15px;
`;

const BgOpacityFrame = styled.div`
  position: absolute;
  width: 100%;
  height: 100%;
  background-color: black;
  opacity: 0.3;
`;

const Content = styled.div`
  position: absolute;
  font-display: flex;
	display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  height: 100%;
  font-size: 36px;
  font-weight: 700;
  color: white;
`;

const LogoBox = styled.div`
  position: relative;
  min-width: 32px;
  width: 64px;
  height: 64px;
`;

const InnerBox1 = styled.div`
	display: flex;
	flex-direction: column;
	width: 50%;
	justify-content: space-between;
	margin-left: 50px;
	margin-top: 40px;
	margin-bottom: 40px;
`;

const MiniBox1 = styled.div`
	display: flex;
	flex-direction: column;
	width: 100%;
	/* height: 30%; */

`;

const MiniBox2 = styled.div`
	display: flex;
	flex-direction: column;
	width: 100%;
	/* height: 30%; */
	margin-bottom: 150px;
	justify-content: space-between;
`;

const MiniBox3 = styled.div`
	display: flex;
	width: 100%;
	/* height: 40%; */

`;

const MiniMini1 = styled.div`
	display: flex;
	width: 100%;
	height: 65%;
`;

const MiniMini2 = styled.div`
	display: flex;
	flex-direction: column;
	width: 100%;
	height: 35%;
`;

const InnerBox2 = styled.div`
	display: flex;
	justify-content: center;
	align-items: center;
	width: 40%;
	min-width: 440px;
	height: 100%;
`;

const index = () => {
  const [windowHeight, setWindowHeight] = useState<number>();

  useEffect(function mount() {
    setWindowHeight(window.innerHeight);
    window.addEventListener("resize", function () {
      setWindowHeight(window.innerHeight);
    });
  });

  return (
    <Container style={{ height: windowHeight }}>
      <Image
        src="/assets/images/signup_img.jpg"
				layout="fill"
        objectFit="cover"
      ></Image>
      <BgOpacityFrame></BgOpacityFrame>
      <Content>
				<InnerBox1 style={{ height: windowHeight - 80 }}>
					<MiniBox1>
						<LogoBox>
							<Image 
								src="/assets/logos/pinset_logo_white.png"
								layout="fill"
								objectFit="contain"	
							></Image>
						</LogoBox>
					</MiniBox1>
					<MiniBox2>
						<TextBox1>당신이 꿈꾸던 공간을...</TextBox1>
						<TextBox2>
							2,490,795 개의 작업공간을 무료료 고해상도로 
							어디에서든 구경할 수 있습니다
						</TextBox2>
					</MiniBox2>
					<MiniBox3>
						<TextBox3>"amy"님이 3년 전에 업로드한 사진입니다</TextBox3>
					</MiniBox3>
				</InnerBox1>
        <InnerBox2>
					<Modal></Modal>
				</InnerBox2>
      </Content>
    </Container>
  );
};

export default index;