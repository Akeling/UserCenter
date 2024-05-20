import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
import React from 'react';
import {PLANET_LINK} from "@/constants";

const Footer: React.FC = () => {
  return (
    <DefaultFooter
      style={{
        background: 'none',
      }}
      links={[
        {
          key: 'planet',
          title: '用户中心',
          href: PLANET_LINK,
          blankTarget: true,
        },
        {
          key: 'github',
          title: <GithubOutlined />,
          href: 'https://github.com/Akeling',
          blankTarget: true,
        },
        {
          key: 'codeNav',
          title: '什羊主页',
          href: 'https://github.com/Akeling',
          blankTarget: true,
        },
      ]}
    />
  );
};

export default Footer;
