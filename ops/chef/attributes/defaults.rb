
default['brightspot']['project'] = 'jordanspieth'
default['brightspot']['project_git_repo'] = 'git@github.com:perfectsense/jordanspieth.git'
default['brightspot']['rootDomain'] = 'jordanspieth.internal'
default['brightspot']['fqdn'] = ''
default['brightspot']['aliases'] = []
default['brightspot']['version'] = 3.0

default['jenkins']['fqdn'] = 'upgrade-jenkins.jordanspieth.psdops.com'

env_secrets = Chef::EncryptedDataBagItem.load("passwords-#{node.chef_environment}", "brightspot")
default['mod-dims']['secret'] = env_secrets['dims_secret']

default['brightspot']['backup_bucket'] = 'jordanspieth-ops'

if node.chef_environment == 'production' or node.chef_environment == 'qa'
  default['brightspot']['context.xml']['dari/debugUsername'] = 'debug'
  default['brightspot']['context.xml']['dari/debugPassword'] = env_secrets['debug_password']
end

default['brightspot']['context.xml']['dari/defaultStorage'] = 'psddevS3'
default['brightspot']['context.xml']['dari/storage/psddevS3/bucket'] = 'jordanspieth'
default['brightspot']['context.xml']['dari/storage/psddevS3/class'] = 'com.psddev.dari.util.AmazonStorageItem'
default['brightspot']['context.xml']['dari/storage/psddevS3/baseUrl'] = 'http://jordanspieth.s3.amazonaws.com'
default['brightspot']['context.xml']['dari/storage/psddevS3/access'] = env_secrets['aws_access']
default['brightspot']['context.xml']['dari/storage/psddevS3/secret'] = env_secrets['aws_secret']

default['brightspot']['context.xml']['dari/defaultImageEditor'] = 'dims'
default['brightspot']['context.xml']['dari/imageEditor/dims/baseUrl'] = '/dims4/default'
default['brightspot']['context.xml']['dari/imageEditor/dims/sharedSecret'] = env_secrets['dims_secret']
default['brightspot']['context.xml']['dari/imageEditor/dims/useLegacyThumbnail'] = false
default['brightspot']['context.xml']['dari/imageEditor/dims/quality'] = 90

default['brightspot']['context.xml']['cookieSecret'] = '07660d782448792a17298d818494acd4'

default['brightspot']['redirectors'].tap do |redirectors|
  redirectors['redirector1'].tap do |redirector|
    redirector['domain'] = 'jordanspiethgolf.com'
    redirector['fqdn'] = 'www.jordanspiethgolf.com'
  end
end
