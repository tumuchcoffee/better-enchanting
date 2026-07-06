{pkgs}: {
  deps = [
    pkgs.maven
    pkgs.gradle
    pkgs.temurin-bin
    pkgs.openjdk
    pkgs.jdk24
    pkgs.jdk23
    pkgs.jdk21
    pkgs.jdk17
    pkgs.jdk
  ];
}
